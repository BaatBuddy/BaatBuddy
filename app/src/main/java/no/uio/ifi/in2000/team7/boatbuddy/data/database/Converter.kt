package no.uio.ifi.in2000.team7.boatbuddy.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team7.boatbuddy.model.preference.WeatherPreferences
import java.util.prefs.Preferences

class Converter {
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

    @TypeConverter
    fun fromPreferences(preferences: WeatherPreferences?): String? {
        return gson.toJson(preferences)
    }

    @TypeConverter
    fun toPreferences(preferences: String?): WeatherPreferences? {
        if (preferences == null) {
            return null
        }
        val preferencesType = object : TypeToken<WeatherPreferences>() {}.type
        return gson.fromJson<WeatherPreferences>(preferences, preferencesType)
    }
}