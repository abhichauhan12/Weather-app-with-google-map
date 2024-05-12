package com.abhishek.weatherapp.presentation.weather

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.weatherapp.R
import com.abhishek.weatherapp.presentation.WeatherViewModel
import com.abhishek.weatherapp.presentation.map.CurrentLocation
import com.abhishek.weatherapp.ui.theme.BlueLight
import com.abhishek.weatherapp.ui.theme.BlueTwo
import com.google.android.gms.location.FusedLocationProviderClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun WeatherUI(fusedLocationProviderClient: FusedLocationProviderClient) {

    val viewModel : WeatherViewModel = hiltViewModel()
    val weatherLatLon = viewModel.weatherLatLon.collectAsState().value
    val weatherCity = viewModel.weatherCity.collectAsState().value
    val error = viewModel.error.collectAsState().value

    var searchText by remember { mutableStateOf("") }

    val context = LocalContext.current

    ConstraintLayout {
        val (mapUi , weatherUi ,searchBar ) = createRefs()

        Box (modifier = Modifier
            .constrainAs(mapUi){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(weatherUi.top)

            }
        ) {

            CurrentLocation(fusedLocationProviderClient = fusedLocationProviderClient)

        }


        Box (modifier = Modifier
            .constrainAs(searchBar) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
            .padding(horizontal = 32.dp, vertical = 16.dp)
        ){
            ConstraintLayout(
            ) {
                val (searchFieldText , searchButton) = createRefs()
                val keyboardController = LocalSoftwareKeyboardController.current

                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp,)
                        .background(Color.LightGray, RoundedCornerShape(50.dp))
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .constrainAs(searchFieldText) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(searchButton.start, margin = 8.dp)
                        },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 16.sp),
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp)
                            .background(Color.LightGray, RoundedCornerShape(50.dp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (searchText.isEmpty()) {
                                Text(
                                    "Enter location",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }else{
                                innerTextField()
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    )
                )

                val cityName = searchText

                Button(
                    onClick = {
                        if (cityName.isEmpty()){
                            Toast.makeText(context, "Please enter the city name", Toast.LENGTH_LONG).show()
                        }else{
                            viewModel.getWeather(cityName)
                            keyboardController?.hide()
                            searchText =""

                            Toast.makeText(context, "Featching Weather", Toast.LENGTH_SHORT).show()

                        }
                    },
                    modifier= Modifier
                        .size(50.dp)
                        .constrainAs(searchButton) {
                            start.linkTo(searchFieldText.end)
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(searchFieldText.top)
                            bottom.linkTo(searchFieldText.bottom)
                        }
                    ,
                    contentPadding = PaddingValues(8.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.filledTonalButtonColors(BlueTwo)
                ) {

                    Icon( painterResource(id = R.drawable.ic_search) ,
                        contentDescription ="Search",
                        tint = Color.White)
                }

            }
        }



        Box(modifier = Modifier
            .constrainAs(weatherUi) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            .background(BlueLight, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .padding(bottom = 24.dp)
        ) {

            if (weatherCity != null) {
                WeatherDetail(
                    cityName = weatherCity.name,
                    tempe = weatherCity.main.temp.toString(),
                    weatherDesc = weatherCity.weather[0].description,
                    weatherIcon = weatherCity.weather[0].id,

                )
            } else if (weatherLatLon != null) {
                WeatherDetail(
                    cityName = weatherLatLon.name,
                    tempe = weatherLatLon.main.temp.toString(),
                    weatherDesc = weatherLatLon.weather[0].description,
                    weatherIcon = weatherLatLon.weather[0].id,
                )
            }else {
                Text(text = "Error city not found")
            }



        }

    }



    error?.let {
        Toast.makeText(
            context,
            "Error: City not Found \n Back to current Location ",
            Toast.LENGTH_LONG
        ).show()
    }

}

