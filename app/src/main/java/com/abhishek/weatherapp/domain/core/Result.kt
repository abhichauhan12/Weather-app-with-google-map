package com.abhishek.weatherapp.domain.core

import com.abhishek.weatherapp.data.network.FetchStatus

data class Result<T>(val status : FetchStatus, val data: T?)
