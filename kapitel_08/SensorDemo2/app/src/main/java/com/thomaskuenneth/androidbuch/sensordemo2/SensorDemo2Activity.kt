package com.thomaskuenneth.androidbuch.sensordemo2

import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.util.*

class SensorDemo2Activity : AppCompatActivity() {
    private val key1 = "shouldCallWaitForTriggerInOnResume"
    private val key2 = "tv"
    private val dateFormat = DateFormat.getTimeInstance()
    private val listener = object : TriggerEventListener() {
        override fun onTrigger(event: TriggerEvent) {
            shouldCallWaitForTriggerInOnResume = false
            button.visibility = View.VISIBLE
            textview.text = dateFormat.format(Date())
        }
    }
    private var shouldCallWaitForTriggerInOnResume = false

    private lateinit var manager: SensorManager

    private var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            shouldCallWaitForTriggerInOnResume = true
            waitForTrigger()
        }
        manager = getSystemService(SensorManager::class.java)
        sensor = manager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION)
        if (sensor == null) {
            shouldCallWaitForTriggerInOnResume = false
            button.visibility = View.GONE
            textview.setText(R.string.no_sensors)
        } else {
            shouldCallWaitForTriggerInOnResume = true
            if (savedInstanceState != null) {
                shouldCallWaitForTriggerInOnResume = savedInstanceState.getBoolean(key1)
                textview.text = savedInstanceState.getString(key2)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(key1, shouldCallWaitForTriggerInOnResume)
        outState.putString(key2, textview.text.toString())
    }

    override fun onResume() {
        super.onResume()
        if (sensor != null) {
            if (shouldCallWaitForTriggerInOnResume) {
                waitForTrigger()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (sensor != null) {
            manager.cancelTriggerSensor(listener, sensor)
        }
    }

    private fun waitForTrigger() {
        button.visibility = View.GONE
        textview.setText(R.string.wait)
        manager.requestTriggerSensor(listener, sensor)
    }
}