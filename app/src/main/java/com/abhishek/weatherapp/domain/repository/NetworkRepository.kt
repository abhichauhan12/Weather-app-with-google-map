package com.abhishek.weatherapp.domain.repository

import android.util.Log
import com.abhishek.weatherapp.BuildConfig
import com.abhishek.weatherapp.data.network.FetchStatus
import com.abhishek.weatherapp.data.network.apis.WeatherApi
import com.abhishek.weatherapp.data.network.apis.WeatherLatLonApi
import com.abhishek.weatherapp.data.network.response.WeatherResponse
import com.abhishek.weatherapp.domain.core.Result
import com.abhishek.weatherapp.utils.UNITS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val latLonApi: WeatherLatLonApi,
) {

    private val apiKey = BuildConfig.OPEN_WEATHER_API_KEY

    suspend fun getLatLonWeatherData(lat : String,lon:String) :Result<WeatherResponse>{
        return withContext(Dispatchers.IO){
            try {

                val response = latLonApi
                    .getLatLonWeatherData(lat,lon, apiKey  , UNITS )
                    .execute()

                val responseBody =response.body()
                Log.d("API Success", "Response: $responseBody")

                if (response.isSuccessful && responseBody != null){
                    Result(FetchStatus.FETCHED, responseBody )
                }else if (response.code() == 404) {
                    // Handling 404 specifically
                    Log.d("API Error", "Location Not Found")
                    Result(FetchStatus.FAILURE("Location Not Found"), null)
                } else
                    Result(FetchStatus.FAILURE("Failed to fetch data: ${response.code()}"), null)
            }catch (e: Exception){
                Log.d("API Error", "Failed to fetch data: ${e.message}")
                Result(FetchStatus.FAILURE(e.message), null)
            }
        }
    }



    suspend fun getWeatherData(cityName : String) :Result<WeatherResponse>{
        return withContext(Dispatchers.IO){
            try {

                val response = weatherApi
                    .getCurrentWeatherData(cityName, apiKey, UNITS)
                    .execute()

                val responseBody =response.body()
                Log.d("API Success", "Response: $responseBody")

                if (response.isSuccessful && responseBody != null){
                    Result(FetchStatus.FETCHED, responseBody )
                }else if (response.code() == 404) {
                    // Handling 404 specifically
                    Log.d("API Error", "City not found: $cityName")
                    Result(FetchStatus.FAILURE("City not found: $cityName "), null)
                } else
                    Result(FetchStatus.FAILURE("Failed to fetch data: ${response.code()}"), null)
            }catch (e: Exception){
                Log.d("API Error", "Failed to fetch data: ${e.message}")
                Result(FetchStatus.FAILURE(e.message), null)
            }
        }
    }

}