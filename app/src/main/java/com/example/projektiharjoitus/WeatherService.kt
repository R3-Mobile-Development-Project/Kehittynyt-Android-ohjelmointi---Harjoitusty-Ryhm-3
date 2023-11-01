package com.example.projektiharjoitus

import com.example.projektiharjoitus.WeatherData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


// WeatherService.kt
class WeatherService {
    private val apiKey = "a01c3a3ceac8e9ae65a0f70205d467d3"
    private val client = OkHttpClient()

    fun getWeatherData(city: String, callback: (WeatherData) -> Unit) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseString = response.body?.string()
                    val weatherData = parseWeatherData(responseString)
                    callback(weatherData)
                } else {
                    // Handle non-successful response
                }
            }
        })
    }

    private fun parseWeatherData(responseString: String?): WeatherData {
        // Ensure responseString is not null or empty
        if (responseString.isNullOrEmpty()) {
            // Handle the case where the response is empty or null
            return WeatherData(0.0, 0, "No Data")
        }

        try {
            val jsonObject = JSONObject(responseString)

            // Extract temperature, humidity, and description from the JSON response
            val main = jsonObject.getJSONObject("main")
            val temperature = main.getDouble("temp")
            val humidity = main.getInt("humidity")
            val weatherArray = jsonObject.getJSONArray("weather")
            val weatherObject = weatherArray.getJSONObject(0)
            val description = weatherObject.getString("description")

            // Create a WeatherData object and return it
            return WeatherData(temperature, humidity, description)
        } catch (e: Exception) {
            // Handle any parsing exceptions
            e.printStackTrace()
            return WeatherData(0.0, 0, "Parsing Error")
        }
    }
}
