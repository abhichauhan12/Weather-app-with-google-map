package com.abhishek.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abhishek.weatherapp.BuildConfig
import com.abhishek.weatherapp.presentation.weather.WeatherUI
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint as AndroidEntryPoint1

@AndroidEntryPoint1
class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            WeatherUI(fusedLocationProviderClient)
        }
    }

}

