package com.thomaskuenneth.androidbuch.webservicedemo1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.MessageFormat

private const val URL =
    "https://api.openweathermap.org/data/2.5/weather?q={0}&lang=de&appid={1}"
private const val KEY = "..."
private const val NAME = "name"
private const val WEATHER = "weather"
private const val DESCRIPTION = "description"
private const val ICON = "icon"
private const val MAIN = "main"
private const val TEMP = "temp"

@Throws(JSONException::class, IOException::class)
fun getWeather(city: String): WeatherData {
    val data = WeatherData()
    val jsonObject = JSONObject(
        getFromServer(
            MessageFormat.format(
                URL,
                city,
                KEY
            )
        )
    )
    if (jsonObject.has(NAME)) {
        data.name = jsonObject.getString(NAME)
    }
    if (jsonObject.has(WEATHER)) {
        val jsonArrayWeather = jsonObject.getJSONArray(WEATHER)
        if (jsonArrayWeather.length() > 0) {
            val jsonWeather = jsonArrayWeather.getJSONObject(0)
            data.description = jsonWeather.optString(DESCRIPTION)
            data.icon = jsonWeather.optString(ICON)
        }
    }
    if (jsonObject.has(MAIN)) {
        val main = jsonObject.getJSONObject(MAIN)
        data.temp = main.optDouble(TEMP)
    }
    return data
}

@Throws(IOException::class)
fun getImage(w: WeatherData): Bitmap {
    val url = URL("https://openweathermap.org/img/w/${w.icon}.png")
    val connection = url.openConnection() as HttpURLConnection
    val bitmap = BitmapFactory.decodeStream(connection.inputStream)
    connection.disconnect()
    return bitmap
}

private fun getFromServer(url: String): String {
    val sb = StringBuilder()
    val connection = URL(url).openConnection() as HttpURLConnection
    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
        InputStreamReader(
            connection.inputStream
        ).use { inputStreamReader ->
            BufferedReader(
                inputStreamReader
            ).use {
                it.lines().forEach { line ->
                    sb.append(line)
                }
            }
        }
    }
    connection.disconnect()
    return sb.toString()
}