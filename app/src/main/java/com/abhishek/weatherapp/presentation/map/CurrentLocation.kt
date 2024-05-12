package com.abhishek.weatherapp.presentation.map

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.weatherapp.presentation.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@Composable
fun CurrentLocation(fusedLocationProviderClient: FusedLocationProviderClient) {

    val viewModel : WeatherViewModel = hiltViewModel()
    val weatherCity = viewModel.weatherCity.collectAsState().value

    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }

    val currentLatLon = LocationUtils.getPosition(currentLocation)

    // Remember the latest effective location, updating when weatherCity changes
    val effectiveLocation by remember(weatherCity, currentLocation) {
        mutableStateOf(weatherCity?.let {
            Log.d("cityLatLog", "Using weather city location: ${it.coord.lat}, ${it.coord.lon}")
            LocationUtils.getCityLocation(it.coord.lat, it.coord.lon)
        } ?: currentLocation)
    }

    val cameraPositionState = rememberCameraPositionState()

    // Update the camera position based on the effective location
    LaunchedEffect(effectiveLocation) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            LocationUtils.getPosition(effectiveLocation), 12f
        )
    }

    viewModel.getLatLonWeather(
        currentLatLon.latitude.toString(),
        currentLatLon.longitude.toString()
    )
    var requestLocationUpdate by remember { mutableStateOf(true) }


    MyGoogleMap(
        effectiveLocation,
        cameraPositionState,
    )

    if(requestLocationUpdate) {
        LocationPermissionsAndSettingDialogs(
            updateCurrentLocation = {
                requestLocationUpdate = false
                LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->
                    locationResult.lastLocation?.let { location ->
                        Log.d("LocationUpdate", "New Location: ${location.latitude}, ${location.longitude}")
                        currentLocation = location
                    }

                }
            }
        )
    }
}

@Composable
private fun MyGoogleMap(
    location: Location,
    cameraPositionState: CameraPositionState,

    ) {

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(zoomControlsEnabled = false)
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        ) {
        Marker(
            state = MarkerState(position = LocationUtils.getPosition(location)),
            title = "Current Position"
        )
    }

}