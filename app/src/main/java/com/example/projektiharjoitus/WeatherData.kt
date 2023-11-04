package com.example.projektiharjoitus

import androidx.compose.foundation.pager.PageSize
import java.text.DecimalFormat

data class WeatherData(
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val humidity: Int,
    val description: String,
    // Add other properties you need to represent weather data

)
{
    val formattedTemperature: String
        get() {
            // Format temperature to one decimal place
            val decimalFormat = DecimalFormat("#.#")
            return "${decimalFormat.format(temperature - 273)}"
        }
}



