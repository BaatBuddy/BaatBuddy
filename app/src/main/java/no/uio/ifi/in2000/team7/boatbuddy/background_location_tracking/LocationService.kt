package no.uio.ifi.in2000.team7.boatbuddy.background_location_tracking

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import no.uio.ifi.in2000.team7.boatbuddy.R
import no.uio.ifi.in2000.team7.boatbuddy.background_location_tracking.FeatureDataCache.featureData
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.AlertPolygon
import no.uio.ifi.in2000.team7.boatbuddy.model.metalerts.FeatureData
import kotlin.math.max
import kotlin.math.min


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

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.avd_boat)
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        locationClient
            .getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude
                val alerts = checkUserLocationAlertAreas(lon = 5.012127, lat = 59.323324)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)\n${alerts.map { it.event + " " }}\n${alerts.size}"
                )
                notificationManager.notify(1, updatedNotification.build()) // Updates notification
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())

    }

    private fun stop() {
        stopForeground(true) // Removes notification
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
        FeatureDataCache.featureData = featureData
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
        Log.i("ASDASD", asd.toString())
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















