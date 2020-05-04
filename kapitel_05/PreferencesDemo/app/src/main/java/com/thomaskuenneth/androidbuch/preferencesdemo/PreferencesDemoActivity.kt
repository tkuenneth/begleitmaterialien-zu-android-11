package com.thomaskuenneth.androidbuch.preferencesdemo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class PreferencesDemoActivity : AppCompatActivity() {

    private val rqSettings = 1234

    private lateinit var textview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(
                this,
                SettingsActivity::class.java
            )
            startActivityForResult(intent, rqSettings)
        }
        textview = findViewById(R.id.textview)
        updateTextView()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (rqSettings == requestCode) {
            updateTextView()
        }
    }

    private fun updateTextView() {
        val prefs = PreferenceManager
            .getDefaultSharedPreferences(this)
        val cb1 = if (prefs.contains("checkbox_1"))
            prefs.getBoolean("checkbox_1", false)
        else
            getString(R.string.not_set)
        val cb2 = if (prefs.contains("checkbox_2"))
            prefs.getBoolean("checkbox_2", false)
        else
            getString(R.string.not_set)
        val et1 = if (prefs.contains("edittext_1"))
            prefs.getString("edittext_1", "")
        else
            getString(R.string.not_set)
        textview.text = getString(
            R.string.template,
            cb1.toString(), cb2.toString(), et1
        )
    }
}