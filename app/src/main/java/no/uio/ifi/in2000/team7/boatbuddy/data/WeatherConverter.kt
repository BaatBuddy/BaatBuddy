package no.uio.ifi.in2000.team7.boatbuddy.data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

object WeatherConverter {
    // fetches drawable from a english alert event
    @SuppressLint("DiscouragedApi")
    fun convertAlertResId(
        event: String,
        riskMatrixColor: String,
        context: Context
    ): Int {

        val iconName = when (event) {
            "avalanches" -> "icon_warning_avalanches"
            "blowingSnow" -> "icon_warning_snow"
            "drivingConditions" -> "icon_warning_drivingconditions"
            "flood" -> "icon_warning_flood"
            "forestFire" -> "icon_warning_forestfire"
            "gale" -> "icon_warning_wind"
            "ice" -> "icon_warning_ice"
            "icing" -> "icon_warning_generic"
            "landSlide" -> "icon_warning_landslide"
            "polarLow" -> "icon_warning_polarlow"
            "rain" -> "icon_warning_rain"
            "rainFlood" -> "icon_warning_rainflood"
            "snow" -> "icon_warning_snow"
            "stormSurge" -> "icon_warning_stormsurge"
            "lightning" -> "icon_warning_lightning"
            "wind" -> "icon_warning_wind"
            else -> "icon_warning_generic" // "unknown"
        } + "_" + if (riskMatrixColor.isBlank()) "yellow" else riskMatrixColor.lowercase()

        return context.resources.getIdentifier(iconName, "drawable", context.packageName)
    }

    // translates english weather alert to norwegian
    fun convertLanguage(alertEvent: String): String {
        return when (alertEvent) {
            "avalanches" -> "Jordskred"
            "blowingSnow" -> "Snøfokk"
            "drivingConditions" -> "Kjøreforhold"
            "flood" -> "Flom"
            "forestFire" -> "Skogbrannfare"
            "gale" -> "Kuling"
            "ice" -> "Is"
            "icing" -> "Ising"
            "landSlide" -> "Ras"
            "polarLow" -> "Polart lavtrykk"
            "rain" -> "Regn"
            "rainFlood" -> "Styrtregn"
            "snow" -> "Snø"
            "stormSurge" -> "Stormflo"
            "lightning" -> "Mye lyn"
            "wind" -> "Vindkast"
            else -> "Generisk"
        }
    }

    // fetches drawable based on string
    @SuppressLint("DiscouragedApi")
    fun convertWeatherResId(
        symbolCode: String,
        context: Context
    ): Int {
        return context.resources.getIdentifier(symbolCode, "drawable", context.packageName)
    }

    // functions to convert a xml vector to a bitmap object
    fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int): Bitmap? =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    // translates english weather terms to norwegian
    fun translateSymbolCode(symbolCode: String): String {
        return when (symbolCode.replace("_", "").replace("night", "").replace("day", "")) {
            "clearsky" -> "Klarvær"
            "fair" -> "Lettskyet"
            "partlycloudy" -> "Delvis skyet"
            "cloudy" -> "Skyet"
            "lightrainshowers" -> "Lette regnbyger"
            "rainshowers" -> "Regnbyger"
            "heavyrainshowers" -> "Kraftige regnbyger"
            "lightrainshowersandthunder" -> "Lette regnbyger og torden"
            "rainshowersandthunder" -> "Regnbyger og torden"
            "heavyrainshowersandthunder" -> "Kraftige regnbyger og torden"
            "lightsleetshowers" -> "Lette sluddbyger"
            "sleetshowers" -> "Sluddbyger"
            "heavysleetshowers" -> "Kraftige sluddbyger"
            "lightssleetshowersandthunder" -> "Lette sluddbyger og torden"
            "sleetshowersandthunder" -> "Sluddbyger og torden"
            "heavysleetshowersandthunder" -> "Kraftige sluddbyger og torden"
            "lightsnowshowers" -> "Lette snøbyger"
            "snowshowers" -> "Snøbyger"
            "heavysnowshowers" -> "Kraftige snøbyger"
            "lightssnowshowersandthunder" -> "Lette snøbyger og torden"
            "snowshowersandthunder" -> "Snøbyger og torden"
            "heavysnowshowersandthunder" -> "Kraftige snøbyger og torden"
            "lightrain" -> "Lett regn"
            "rain" -> "Regn"
            "heavyrain" -> "Kraftig regn"
            "lightrainandthunder" -> "Lett regn og torden"
            "rainandthunder" -> "Regn og torden"
            "heavyrainandthunder" -> "Kraftig regn og torden"
            "lightsleet" -> "Lett sludd"
            "sleet" -> "Sludd"
            "heavysleet" -> "Kraftig sludd"
            "lightsleetandthunder" -> "Lett sludd og torden"
            "sleetandthunder" -> "Sludd og torden"
            "heavysleetandthunder" -> "Kraftig sludd og torden"
            "lightsnow" -> "Lett snø"
            "snow" -> "Snø"
            "heavysnow" -> "Kraftig snø"
            "lightsnowandthunder" -> "Lett snø og torden"
            "snowandthunder" -> "Snø og torden"
            "heavysnowandthunder" -> "Kraftig snø og torden"
            "fog" -> "Tåke"
            else -> ""
        }
    }
}