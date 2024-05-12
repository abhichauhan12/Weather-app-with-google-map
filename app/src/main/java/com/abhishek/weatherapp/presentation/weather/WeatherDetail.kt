package com.abhishek.weatherapp.presentation.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.abhishek.weatherapp.R

@Composable
fun WeatherDetail(cityName : String,
                  tempe : String,
                  weatherDesc : String,
                  weatherIcon : Int
                  ){

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val( weatherImage , temperature , placeName , weatherDescription ) = createRefs()

        Row(
            modifier = Modifier
                .constrainAs(placeName) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(start = 16.dp, top = 16.dp),
        ) {


            Image(painterResource(id =R.drawable.ic_location),
                contentDescription ="Location",
                modifier = Modifier.size(16.dp))

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = cityName , fontSize = 16.sp )

        }



        Text(
            modifier = Modifier
                .constrainAs(temperature) {
                    start.linkTo(parent.start)
                    top.linkTo(placeName.bottom)
                }
                .padding(start = 16.dp, top = 4.dp),
            fontSize = 54.sp,
            text = "$tempe°c")

        Text(
            modifier = Modifier
                .constrainAs(weatherDescription) {
                    start.linkTo(parent.start)
                    top.linkTo(temperature.bottom)
                }
                .padding(start = 16.dp),
            fontSize = 24.sp,
            text = weatherDesc)


        var a : Int = 0
        var iconOfWeather : Int = 0

        when (weatherIcon) {
            800 -> {
                iconOfWeather = R.drawable.ic_sun_clean
            }
            801 -> {
                iconOfWeather = R.drawable.ic_fewclouds
            }
            in 802..804 -> {
                iconOfWeather = R.drawable.ic_clouds
            }
            in 300..399 -> {
                iconOfWeather = R.drawable.ic_rain
            }
            in 500..599 -> {
                iconOfWeather = R.drawable.ic_rain
            }
            in 200..299 -> {
                iconOfWeather = R.drawable.ic_thunderstorm
            }
            in 600..699 -> {
                iconOfWeather = R.drawable.ic_snow
            }
            in 700..799 -> {
                iconOfWeather = R.drawable.ic_mist
            }
        }


        Image(painterResource(id = iconOfWeather) ,
            contentDescription ="",
            modifier = Modifier
                .constrainAs(weatherImage) {
                    end.linkTo(parent.end, margin = 16.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(80.dp)
        )
    }
    

}