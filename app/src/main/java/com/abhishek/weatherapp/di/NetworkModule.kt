package com.abhishek.weatherapp.di

import com.abhishek.weatherapp.data.network.APIService
import com.abhishek.weatherapp.data.network.apis.WeatherApi
import com.abhishek.weatherapp.data.network.apis.WeatherLatLonApi
import com.abhishek.weatherapp.utils.BASE_URL
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    @Provides
    fun provideAPIService(retrofit: Retrofit) : APIService {
        return APIService(retrofit = retrofit)
    }

    @Provides
    fun provideWeatherAPI(apiService: APIService) : WeatherApi {
        return apiService.createAPI(WeatherApi::class.java)
    }

    @Provides
    fun provideLatLonAPI(apiService: APIService) : WeatherLatLonApi {
        return apiService.createAPI(WeatherLatLonApi::class.java)
    }


}