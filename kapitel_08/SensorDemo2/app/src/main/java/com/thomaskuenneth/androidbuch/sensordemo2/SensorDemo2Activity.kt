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

private const val KEY1 = "shouldCallWaitForTriggerInOnResume"
private const val KEY2 = "tv"
class SensorDemo2Activity : AppCompatActivity() {
    private val dateFormat = DateFormat.getTimeInstance()
    private val listener = object : TriggerEventListener() {
        override fun onTrigger(event: TriggerEvent) {
            shouldCallWaitForTriggerInOnResume = false
            button.visibility = View.VISIBLE
            textview.text = dateFormat.format(Date())
        }
    }
    private var shouldCallWaitForTriggerInOnResume = false
    private var sensor: Sensor? = null
    private lateinit var manager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            shouldCallWaitForTriggerInOnResume = true
            waitForTrigger()
        }
        manager = getSystemService(SensorManager::class.java)
        sensor = manager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION)
        sensor?.also {
            shouldCallWaitForTriggerInOnResume = true
            savedInstanceState?.run {
                shouldCallWaitForTriggerInOnResume = getBoolean(KEY1)
                textview.text = getString(KEY2)
            }
        } ?: run {
            shouldCallWaitForTriggerInOnResume = false
            button.visibility = View.GONE
            textview.setText(R.string.no_sensors)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY1, shouldCallWaitForTriggerInOnResume)
        outState.putString(KEY2, textview.text.toString())
    }

    override fun onResume() {
        super.onResume()
        sensor?.let {
            if (shouldCallWaitForTriggerInOnResume) {
                waitForTrigger()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensor?.let {
            manager.cancelTriggerSensor(listener, sensor)
        }
    }

    private fun waitForTrigger() {
        button.visibility = View.GONE
        textview.setText(R.string.wait)
        manager.requestTriggerSensor(listener, sensor)
    }
}