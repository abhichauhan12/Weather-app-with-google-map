package com.abhishek.weatherapp.data.network.apis

import com.abhishek.weatherapp.data.network.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("q") city : String,
        @Query("appid") appid : String,
        @Query("units") units : String,
    ) : Call<WeatherResponse>
}