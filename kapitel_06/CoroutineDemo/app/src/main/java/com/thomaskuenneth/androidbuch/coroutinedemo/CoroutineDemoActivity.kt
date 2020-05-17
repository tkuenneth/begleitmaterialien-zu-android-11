package com.thomaskuenneth.androidbuch.coroutinedemo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class CoroutineDemoActivity : AppCompatActivity() {

    private lateinit var textview: TextView

    /* Um beim Verlassen der Activity laufende Koroutinen
       zu beenden, übernehmen Sie bitte die folgenden
       Änderungen:
       
class CoroutineDemoActivity : AppCompatActivity(), CoroutineScope {
  ...
  override val coroutineContext = SupervisorJob() + Dispatchers.IO

  override fun onPause() {
    super.onPause()
    cancel(null)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    ..
    button.setOnClickListener {
      launch {
        pause((1 + Math.random() * 10).toLong())
      }
...
     */

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