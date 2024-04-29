package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.geojson.Point

class PointConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromPointList(pointList: List<Point>?): String? {
        return gson.toJson(pointList)
    }

    @TypeConverter
    fun toPointList(pointListString: String?): List<Point>? {
        if (pointListString == null) {
            return null
        }
        val listType = object : TypeToken<List<Point>>() {}.type
        return gson.fromJson<List<Point>>(pointListString, listType)
    }
}