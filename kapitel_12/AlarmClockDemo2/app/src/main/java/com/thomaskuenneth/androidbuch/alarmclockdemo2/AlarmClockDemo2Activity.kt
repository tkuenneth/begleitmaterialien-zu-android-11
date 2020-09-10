package com.thomaskuenneth.androidbuch.alarmclockdemo2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class AlarmClockDemo2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        textview.text = getString(R.string.no_intent)
        intent?.run {
            textview.text = String.format("%s\n", action)
            extras?.let { bundle ->
                bundle.keySet()?.let { keys ->
                    keys.forEach {
                        textview.append("$it\n")
                        textview.append("${bundle[it]}\n\n")
                    }
                }
            }
        }
    }
}