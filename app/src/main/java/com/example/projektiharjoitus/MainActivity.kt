package com.example.projektiharjoitus

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity() {
    private val weatherService = WeatherService()
    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private var previousLocation: LatLng? = null
    private var previousCity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the SupportMapFragment
        mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
        }

        // Check for and request permissions (use appropriate code)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val weatherInfoTextView = findViewById<TextView>(R.id.weatherInfoTextView)
        val fetchWeatherButton = findViewById<Button>(R.id.fetchWeatherButton)
        val cityInputEditText = findViewById<EditText>(R.id.cityInputEditText)

        // Set the input type to capitalize sentences
        cityInputEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

        fetchWeatherButton.setOnClickListener {
            val city = cityInputEditText.text.toString().trim()
            if (city.isNotEmpty()) {
                // Check if the city has changed
                if (previousCity == null || city != previousCity) {
                    try {
                    weatherService.getWeatherData(city) { weatherData ->
                        // Handle the weather data, update UI, etc.
                        runOnUiThread {
                            val newLocation = LatLng(weatherData.latitude, weatherData.longitude)

                            // Check if temperature is -273
                            val weatherInfo = if (weatherData.formattedTemperature == "-273") {
                                "${weatherData.description}"
                            } else {
                                "Temperature: ${weatherData.formattedTemperature} °C\nDescription: ${weatherData.description} \nHumidity: ${weatherData.humidity}%"
                            }

                            // Update the UI with the weather data
                            weatherInfoTextView.text = weatherInfo

                            // Move the camera to the new location
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 12f))

                            // Update the previous city
                            previousCity = city
                        }
                    }
                    } catch (error: Exception) {
                        // Handle the error (e.g., show an error message to the user)
                        weatherInfoTextView.text = "Location not found"
                        googleMap.clear()
                    }
                } else {
                    // Handle case when the city is the same as the previous search
                    weatherInfoTextView.text = "Location is the same as the previous search."
                    // Clear the map
                    googleMap.clear()
                }
            } else {
                // Handle case when the city input is empty
                weatherInfoTextView.text = "Please enter a location."
                // Clear the map
                googleMap.clear()
            }
        }
    }

    // Handle the result of the permission request (use appropriate code)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // Call superclass implementation

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed
            } else {
                // Permission denied, handle this case
            }
        }
    }
}


