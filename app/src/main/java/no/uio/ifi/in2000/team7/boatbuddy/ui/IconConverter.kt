package no.uio.ifi.in2000.team7.boatbuddy.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

object IconConverter {
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

    @SuppressLint("DiscouragedApi")
    fun convertWeatherResId(
        symbolCode: String,
        context: Context
    ): Int {
        return context.resources.getIdentifier(symbolCode, "drawable", context.packageName)
    }

    // functions to convert a xml vector to a bitmap object
    fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
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
}