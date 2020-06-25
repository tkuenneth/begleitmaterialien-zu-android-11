package com.thomaskuenneth.androidbuch.speechrecognitiondemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class SpeechRecognitionDemoActivity : AppCompatActivity() {
    private val requestVoiceRecognition = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { startVoiceRecognitionActivity() }
        // Verfügbarkeit der Spracherkennung prüfen
        val activities = packageManager.queryIntentActivities(Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
        if (activities.size == 0) {
            button.isEnabled = false
            button.text = getString(R.string.not_present)
        }
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        if (requestCode == requestVoiceRecognition
                && resultCode == Activity.RESULT_OK) {
            val matches =
                    data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            matches?.let {
                if (it.size > 0) {
                    textview.text = matches[0]
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startVoiceRecognitionActivity() {
        val intent = Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.prompt))
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE")
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        startActivityForResult(intent, requestVoiceRecognition)
    }
}