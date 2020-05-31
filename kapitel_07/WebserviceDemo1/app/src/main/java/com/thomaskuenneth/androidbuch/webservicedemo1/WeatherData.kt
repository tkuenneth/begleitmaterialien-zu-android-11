package com.thomaskuenneth.androidbuch.webservicedemo1

data class WeatherData(
    var name: String = "",
    var description: String = "",
    var icon: String = "",
    var temp: Double = 0.0
)