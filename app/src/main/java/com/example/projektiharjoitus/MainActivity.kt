package com.example.projektiharjoitus

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.FragmentActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {
    private val weatherService = WeatherService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check for and request permissions (use appropriate code)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val weatherInfoTextView = findViewById<TextView>(R.id.weatherInfoTextView)

        // Example: Request weather data for a specific city
        weatherService.getWeatherData("CityName") { weatherData ->
            // Handle the weather data, update UI, etc.
            runOnUiThread {
                // Update the UI with the weather data
                val weatherInfo = "Temperature: ${weatherData.temperature} °C\\nDescription: ${weatherData.description}"
                weatherInfoTextView.text = weatherInfo
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

