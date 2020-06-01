package com.thomaskuenneth.androidbuch.sensordemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.DynamicSensorCallback
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private val TAG = SensorDemoActivity::class.simpleName

class SensorDemoActivity : AppCompatActivity() {

    private lateinit var manager: SensorManager

    private val map = HashMap<String, Boolean>()
    private val listener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor,
                                       accuracy: Int) {
            Log.d(TAG, "onAccuracyChanged(): $accuracy")
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.values.isNotEmpty()) {
                val light = event.values[0]
                var text = light.toString()
                if (SensorManager.LIGHT_SUNLIGHT <= light
                        && light <=
                        SensorManager.LIGHT_SUNLIGHT_MAX) {
                    text = getString(R.string.sunny)
                }
                // jeden Wert nur einmal ausgeben
                if (!map.containsKey(text)) {
                    map[text] = true
                    text += "\n"
                    textview.append(text)
                }
            }
        }
    }

    private val callback = object : DynamicSensorCallback() {
        override fun onDynamicSensorConnected(sensor: Sensor) {
            textview.append(getString(R.string.connected,
                    sensor.name))
        }

        override fun onDynamicSensorDisconnected(sensor: Sensor) {
            textview.append(getString(R.string.disconnected,
                    sensor.name))
        }
    }

    private var listenerWasRegistered = false
    private var callbackWasRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        map.clear()
        textview.text = ""
        val mgr = getSystemService(SensorManager::class.java)
        if (mgr == null) {
            finish()
        }
        manager = mgr
        // Liste der vorhandenen Sensoren ausgeben
        manager.getSensorList(Sensor.TYPE_ALL).forEach {
            textview.append(getString(R.string.template,
                    it.name,
                    it.vendor,
                    it.version,
                    it.isDynamicSensor.toString()))
        }
        // Helligkeitssensor ermitteln
        val sensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (sensor != null) {
            manager.registerListener(listener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL)
            listenerWasRegistered = true
        } else {
            textview.append(getString(R.string.no_seonsor))
        }
        // Callback für dynamische Sensoren
        if (manager.isDynamicSensorDiscoverySupported) {
            manager.registerDynamicSensorCallback(callback,
                    Handler())
            callbackWasRegistered = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (listenerWasRegistered) {
            manager.unregisterListener(listener)
        }
        if (callbackWasRegistered) {
            manager.unregisterDynamicSensorCallback(callback)
        }
    }
}