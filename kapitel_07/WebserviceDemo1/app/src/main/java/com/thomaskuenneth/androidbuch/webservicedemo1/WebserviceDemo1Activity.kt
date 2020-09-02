package com.thomaskuenneth.androidbuch.webservicedemo1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

private val TAG = WebserviceDemo1Activity::class.simpleName
class WebserviceDemo1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            thread {
                try {
                    val weather = getWeather(city.text.toString())
                    val bitmapWeather = getImage(weather)
                    runOnUiThread {
                        city.setText(weather.name)
                        image.setImageBitmap(bitmapWeather)
                        beschreibung.text = weather.description
                        val temp = weather.temp - 273.15
                        temperatur.text = getString(
                            R.string.temp_template,
                            temp.toInt()
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "getWeather()", e)
                }
            }
        }
        city.setOnEditorActionListener { _, _, _ ->
            button.performClick()
            true
        }
    }
}