package com.thomaskuenneth.androidbuch.stopwatchdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
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

    private lateinit var model: StopwatchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = ViewModelProviders.of(this).get(StopwatchViewModel::class.java)
        val observer = StopwatchLifecycleObserver(model)
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
            model.running.value?.let {
                var running = it
                running = !running
                model.running.value = running
                if (running) {
                    observer.scheduleAtFixedRate()
                } else {
                    observer.stop()
                }
            }
        }
        reset.setOnClickListener { model.diff.setValue(0L) }
        lifecycle.addObserver(observer)
    }
}