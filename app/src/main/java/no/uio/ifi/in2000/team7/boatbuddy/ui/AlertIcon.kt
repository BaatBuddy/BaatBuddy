package no.uio.ifi.in2000.team7.boatbuddy.ui

import android.annotation.SuppressLint
import android.content.Context

object AlertIcon {
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
}