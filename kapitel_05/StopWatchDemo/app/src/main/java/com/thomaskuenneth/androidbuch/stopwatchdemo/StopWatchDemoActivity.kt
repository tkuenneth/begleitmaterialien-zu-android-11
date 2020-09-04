package com.thomaskuenneth.androidbuch.stopwatchdemo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class StopWatchDemoActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat(
        "HH:mm:ss:SSS",
        Locale.US
    )

    init {
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    private val model: StopWatchDemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val observer = StopWatchDemoLifecycleObserver(model)
        model.running.observe(this, { running: Boolean? ->
            running?.let {
                startStop.setText(if (it) R.string.stop else R.string.start)
                reset.isEnabled = !it
            }
        })
        model.diff.observe(this) { diff: Long? ->
            diff?.let {
                time.text = dateFormat.format(Date(it))
            }
        }
        startStop.setOnClickListener {
            model.running.value?.let { running ->
                if (running) {
                    observer.stop()
                } else {
                    observer.scheduleAtFixedRate()
                }
                model.running.value = !running
            }
        }
        reset.setOnClickListener { model.diff.setValue(0L) }
        lifecycle.addObserver(observer)
    }
}