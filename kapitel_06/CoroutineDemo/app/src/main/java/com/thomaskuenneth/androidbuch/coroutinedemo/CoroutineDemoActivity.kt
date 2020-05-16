package com.thomaskuenneth.androidbuch.coroutinedemo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class CoroutineDemoActivity : AppCompatActivity() {

    private lateinit var textview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textview = findViewById(R.id.textview)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            GlobalScope.launch {
                pause((1 + Math.random() * 10).toLong())
            }
            addToBegin("Button geklickt")
        }
    }

    private suspend fun pause(sec: Long) {
        withContext(Dispatchers.Main) {
            addToBegin("Warte $sec Sekunden")
        }
        delay(1000 * sec)
        withContext(Dispatchers.Main) {
            addToBegin("  ---> $sec Sekunden gewartet")
        }
    }

    private fun addToBegin(s: String) {
        var current = textview.text
        if (current.isNotEmpty()) {
            current = "\n$current"
        }
        textview.text = getString(R.string.template, s, current)
    }
}