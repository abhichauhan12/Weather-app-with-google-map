package com.abhishek.weatherapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.weatherapp.data.network.FetchStatus
import com.abhishek.weatherapp.data.network.response.WeatherResponse
import com.abhishek.weatherapp.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {


    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherLatLon = _weatherData.asStateFlow()

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weatherCity = _weather.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()


    fun getLatLonWeather(lat : String, lon : String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val response =networkRepository.getLatLonWeatherData(lat = lat, lon = lon)
                _weatherData.value = response.data
            }
        }
    }


    fun getWeather(cityName : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = networkRepository.getWeatherData(cityName = cityName)
                _weather.value = response.data

                // handle error (404 city not found)
                if (response.status is FetchStatus.FAILURE){
                    _error.value = response.status.message
                }
            }
        }
    }


}

