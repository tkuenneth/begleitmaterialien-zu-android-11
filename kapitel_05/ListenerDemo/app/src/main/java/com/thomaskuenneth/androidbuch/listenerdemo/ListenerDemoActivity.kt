package com.thomaskuenneth.androidbuch.listenerdemo

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ListenerDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textview = findViewById<TextView>(R.id.textview)
        val checkbox = findViewById<CheckBox>(R.id.checkbox)
        checkbox.setOnClickListener {
            textview.text = checkbox.isChecked.toString()
        }
        val status = findViewById<Button>(R.id.status)
        status.setOnClickListener { checkbox.isChecked = !checkbox.isChecked }
    }
}