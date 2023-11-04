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
            val city = cityInputEditText.text.toString()
            if (city.isNotEmpty()) {
                    weatherService.getWeatherData(city) { weatherData ->
                        // Handle the weather data, update UI, etc.
                        runOnUiThread {
                            if (weatherData.latitude != 0.0 && weatherData.longitude != 0.0) {
                                // Update the UI with the weather data
                                val weatherInfo =
                                    "Temperature: ${weatherData.formattedTemperature} °C\nDescription: ${weatherData.description} \nHumidity: ${weatherData.humidity}%"
                                weatherInfoTextView.text = weatherInfo

                                val cityLocation = LatLng(weatherData.latitude, weatherData.longitude)
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation,12f))
                            } else {
                                // Handle case when the location is not found
                                weatherInfoTextView.text = "Location not found."
                                // Clear the map
                                googleMap.clear()
                            }
                        }
                    }
                } else {
                // Handle case when the city input is empty
                weatherInfoTextView.text = "Please enter a city."
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


