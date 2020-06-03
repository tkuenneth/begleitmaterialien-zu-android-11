package com.thomaskuenneth.androidbuch.sensordemo3

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.no_permission.*

private const val preferencesKey = "last"
fun updateSharedPrefs(
    context: Context?,
    last: Int
) {
    val edit = getSharedPreferences(context)?.edit()
    edit?.putInt(preferencesKey, last)
    edit?.apply()
}

private fun getSharedPreferences(context: Context?) = context?.getSharedPreferences(
    SensorDemo3Activity::class.simpleName,
    Context.MODE_PRIVATE
)

class SensorDemo3Activity : AppCompatActivity(), SensorEventListener {
    private val requestActivityRecognition = 123

    private lateinit var manager: SensorManager

    private var sensor: Sensor? = null
    private var hasSensor = false
    private var last = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = getSystemService(SensorManager::class.java)
        sensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        hasSensor = sensor != null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED
        ) {
            setContentView(R.layout.no_permission)
            button_permission.setOnClickListener {
                requestPermissions(
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    requestActivityRecognition
                )
            }
        } else {
            showStepCounterUi()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestActivityRecognition &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            showStepCounterUi()
        }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val values = sensorEvent.values
        updateSteps(values[0].toInt())
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {
    }

    private fun updateSteps(currentSteps: Int) {
        last = currentSteps
        steps.text = "${currentSteps - getLastStoredStepCount()}"
    }

    private fun getLastStoredStepCount() = getSharedPreferences(this)
        ?.getInt(preferencesKey, 0) ?: 0

    private fun showStepCounterUi() {
        setContentView(R.layout.activity_main)
        reset.setOnClickListener {
            updateSharedPrefs(this, last)
            updateSteps(last)
        }
        on_off.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                manager.registerListener(
                    this, sensor,
                    SensorManager.SENSOR_DELAY_UI
                )
            } else {
                manager.unregisterListener(this)
            }
        }
        on_off.isEnabled = hasSensor
        on_off.isChecked = hasSensor
        reset.isEnabled = hasSensor
        steps.text = getString(
            if (!hasSensor) {
                R.string.no_sensor
            } else {
                R.string.waiting
            }
        )
    }
}