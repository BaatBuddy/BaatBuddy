package no.uio.ifi.in2000.team7.boatbuddy.data.location.foreground_location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.data.PolygonPosition
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter.bitmapFromDrawableRes
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter.convertAlertResId
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter.convertLanguage
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.alertedSunset
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.enteredAlerts
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.finishTime
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.points
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.sdf
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.startTime
import no.uio.ifi.in2000.team7.boatbuddy.data.location.AlertNotificationCache.sunsetToday
import no.uio.ifi.in2000.team7.boatbuddy.data.location.TimeCalculator.isWithinOneHour
import no.uio.ifi.in2000.team7.boatbuddy.data.location.TimeCalculator.sunriseDf
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import no.uio.ifi.in2000.team7.boatbuddy.ui.MainActivity
import java.util.Date


class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    // start tracking process
    private fun start() {
        val locationNotificationId = 1
        alertedSunset = false

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = createPendingIntent()

        // check for version and handle it differently
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val locationChannel = NotificationChannel(
                "location",
                "Location Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val alertChannel =
                NotificationChannel("alert", "Alerts", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(locationChannel)
            notificationManager.createNotificationChannel(alertChannel)
        }

        val locationNotificationBuilder = NotificationCompat.Builder(this, "location")
            .setContentTitle("Sporer lokasjon...")
            .setContentText("Lokasjon: ")
            .setSmallIcon(R.drawable.boatlogo)
            .setOngoing(true)
            .setContentIntent(pendingIntent)

        startForeground(locationNotificationId, locationNotificationBuilder.build())

        locationClient.getLocationUpdates(10L)
            .take(2) // Take only the two first location update
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude
                val lon = location.longitude
                val updatedLocationNotification = locationNotificationBuilder
                    .setContentText("Lokasjon: (lat: $lat, lon: $lon)")
                    .build()
                notificationManager.notify(locationNotificationId, updatedLocationNotification)
            }
            .launchIn(serviceScope)



        locationClient.getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude
                val lon = location.longitude
                val currentAlerts =
                    PolygonPosition.checkUserLocationAlertAreas(
                        lon = lon,
                        lat = lat,
                        AlertNotificationCache.featureData
                    )

                // add points to tracked route
                points.add(Point.fromLngLat(lon, lat))


                // handle time in cache
                if (startTime.isBlank()) {
                    startTime = sdf.format(Date())
                }
                finishTime = sdf.format(Date())

                val updatedLocationNotification = locationNotificationBuilder
                    .setContentText("Lokasjon: (lat: $lat, lon: $lon)")
                    .build()
                notificationManager.notify(locationNotificationId, updatedLocationNotification)

                // check for new alert areas
                val newlyEntered = currentAlerts.map { it.event }.toSet() - enteredAlerts
                for (alert in newlyEntered.map { event -> currentAlerts.first { it.event == event } }) {
                    enteredAlerts.add(alert.event)
                    sendAlertNotification(
                        alert,
                        "Ankommet fareområdet",
                        pendingIntent,
                        notificationManager
                    )
                }

                // check for exited alert areas
                val exited = enteredAlerts - currentAlerts.map { it.event }.toSet()
                for (alert in exited.map { event -> currentAlerts.first { it.event == event } }) {
                    enteredAlerts.remove(alert.event) // Remove the alert from the entered set
                    sendAlertNotification(
                        alert,
                        "Forlatt fareområdet",
                        pendingIntent,
                        notificationManager
                    )
                }
                // send a push notification if user is tracking within one hour of sunset
                if (isWithinOneHour(
                        currentTime = sunriseDf.format(Date()),
                        sunsetTime = sunsetToday
                    ) && !alertedSunset
                ) {
                    val bitmapIcon =
                        bitmapFromDrawableRes(this, R.drawable.night_warning_icon)
                    val alertNotification = NotificationCompat.Builder(this, "alert")
                        .setContentTitle("Snart solnedgang")
                        .setContentText("Det nærmer seg solnedgang")
                        .setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText("Anbefaler å komme seg trygt inn mot land")
                        )
                        .setSmallIcon(R.drawable.night_warning_icon)
                        .setLargeIcon(bitmapIcon)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build()

                    // Use unique IDs for entry/exit to manage separate notifications.
                    val notificationId = 3
                    notificationManager.notify(notificationId, alertNotification)
                    alertedSunset = true
                }

            }.launchIn(serviceScope)
    }

    // metalert notification builder
    private fun sendAlertNotification(
        alert: FeatureData,
        enterExitMessage: String,
        pendingIntent: PendingIntent,
        notificationManager: NotificationManager,
    ) {
        val resId = convertAlertResId(alert.event, alert.riskMatrixColor, this)
        val bitmapIcon =
            bitmapFromDrawableRes(this, resId)
        val alertNotification = NotificationCompat.Builder(this, "alert")
            .setContentTitle("$enterExitMessage: ${convertLanguage(alert.event)}")
            .setContentText("Du befinner deg nå i et værvarselsområde")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Beskrivelse: \n${alert.description}\n\nKonsekvenser:\n${alert.consequences}\n\nInstruksjoner\n${alert.instruction}")
            )
            .setSmallIcon(resId)
            .setLargeIcon(bitmapIcon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Use unique IDs for entry/exit to manage separate notifications.
        val notificationId = ("${alert.event}-$enterExitMessage").hashCode()
        notificationManager.notify(notificationId, alertNotification)
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE) // Removes notification
        stopSelf() // Stops service

    }

    // When service is destroyed we automatically stop tracking location
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


    // Start and Stop Tracking
    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"

    }

    // fill list with data
    fun initializeAlerts(featureData: List<FeatureData>) {
        AlertNotificationCache.featureData = featureData
    }


}















