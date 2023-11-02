package com.example.projektiharjoitus

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity() {
    private val weatherService = WeatherService()
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check for and request permissions (use appropriate code)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val weatherInfoTextView = findViewById<TextView>(R.id.weatherInfoTextView)
        val fetchWeatherButton = findViewById<Button>(R.id.fetchWeatherButton)
        val cityInputEditText = findViewById<EditText>(R.id.cityInputEditText)




        fetchWeatherButton.setOnClickListener {
            val city = cityInputEditText.text.toString()
            if (city.isNotEmpty()) {

                /*
                // Use Geocoder to get the coordinates for the entered city
                val geocoder = Geocoder(this)
                val addresses: List<Address> = geocoder.getFromLocationName(city, 1) ?: emptyList()
                val immutableAddresses = addresses.toList() // Convert to an immutable list

                if (immutableAddresses.isNotEmpty()) {
                    val location = immutableAddresses[0]
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Move the camera to the specified location on the map
                    val cityLocation = LatLng(latitude, longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 10f))
*/
                    weatherService.getWeatherData(city) { weatherData ->
                        // Handle the weather data, update UI, etc.
                        runOnUiThread {
                            // Update the UI with the weather data
                            val weatherInfo =
                                "Temperature: ${weatherData.formattedTemperature} °C\nDescription: ${weatherData.description} \nHumidity: ${weatherData.humidity}%"
                            weatherInfoTextView.text = weatherInfo
                        }
                    }
                }
            /*
            else {
                    // Handle case where the geocoding didn't find the location
                    weatherInfoTextView.text = "Location not found."
                }
            }
            */
            else {
                // Handle case when the city input is empty
                weatherInfoTextView.text = "Please enter a city."
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

