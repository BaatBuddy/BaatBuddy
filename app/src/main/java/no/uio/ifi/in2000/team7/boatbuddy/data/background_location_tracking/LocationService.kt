package no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
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
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter.bitmapFromDrawableRes
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter.convertAlertResId
import no.uio.ifi.in2000.team7.boatbuddy.data.WeatherConverter.convertLanguage
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.AlertNotificationCache.enteredAlerts
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.AlertNotificationCache.featureData
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.AlertNotificationCache.finishTime
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.AlertNotificationCache.points
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.AlertNotificationCache.sdf
import no.uio.ifi.in2000.team7.boatbuddy.data.background_location_tracking.AlertNotificationCache.startTime
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

    //
    private fun start() {
        val locationNotificationId = 1

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = createPendingIntent()

        // Create Notification Channels if Android version is Oreo (API 26) or higher
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
            .setSmallIcon(R.drawable.avd_logosplash)
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
                    checkUserLocationAlertAreas(lon = lon, lat = lat)

                points.add(Point.fromLngLat(lon, lat))

                Log.i("ASDASD", points.toString())

                if (startTime.isBlank()) {
                    startTime = sdf.format(Date())
                }
                finishTime = sdf.format(Date())

                val updatedLocationNotification = locationNotificationBuilder
                    .setContentText("Lokasjon: (lat: $lat, lon: $lon)")
                    .build()
                notificationManager.notify(locationNotificationId, updatedLocationNotification)

                // Identify newly entered alerts
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

                // Identify exited alerts
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
            }.launchIn(serviceScope)
    }

    private fun sendAlertNotification(
        alert: FeatureData,
        enterExitMessage: String,
        pendingIntent: PendingIntent,
        notificationManager: NotificationManager
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

    fun initisializeAlerts(featureData: List<FeatureData>) {
        AlertNotificationCache.featureData = featureData
    }

    // checks if a geopoint is inside a alert polygon
    private fun checkUserLocationAlertAreas(
        lon: Double,
        lat: Double
    ): List<FeatureData> {
        val asd = featureData.filter { featureData ->
            featureData.affected_area.any { area ->
                area.any { polygon ->
                    checkUserLocationPolygon(points = polygon, lon = lon, lat = lat)
                }
            }
        }
        return asd
    }

    // http://www.philliplemons.com/posts/ray-casting-algorithm#:~:text=The%20algorithm%20starts%20with%20P,as%20seen%20in%20Figure%202 + gpt
    fun checkUserLocationPolygon(
        points: List<List<Double>>,
        lon: Double,
        lat: Double
    ): Boolean {

        if (points.size < 3) return false // Not a polygon
        if (points.any { it[0] == lon && it[1] == lat }) return true

        var inside = false
        var prevPoint = points.last()
        for (point in points) {
            val x1 = prevPoint[0]
            val y1 = prevPoint[1]
            val x2 = point[0]
            val y2 = point[1]

            if ((y1 > lat) != (y2 > lat) &&
                (lon < (x2 - x1) * (lat - y1) / (y2 - y1) + x1)
            ) {
                inside = !inside
            }

            prevPoint = point
        }
        return inside
    }

    fun createEdgesFromPoints(points: List<List<Double>>): List<Pair<List<Double>, List<Double>>> {
        return points.mapIndexed { i, point ->
            Pair(point, points[(i + 1) % points.size])
        }
    }

}















