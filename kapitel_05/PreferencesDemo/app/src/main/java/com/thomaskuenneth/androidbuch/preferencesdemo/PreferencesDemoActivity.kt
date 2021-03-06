package com.thomaskuenneth.androidbuch.preferencesdemo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

private const val REQUEST_SETTINGS = 1234
class PreferencesDemoActivity : AppCompatActivity() {

    private lateinit var textview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(
                this,
                SettingsActivity::class.java
            )
            startActivityForResult(intent, REQUEST_SETTINGS)
        }
        textview = findViewById(R.id.textview)
        updateTextView()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_SETTINGS == requestCode) {
            updateTextView()
        }
    }

    private fun updateTextView() {
        val prefs = PreferenceManager
            .getDefaultSharedPreferences(this)
        val cb1 = if (prefs.contains("checkbox_1"))
            prefs.getBoolean("checkbox_1", false).toString()
        else
            getString(R.string.not_set)
        val cb2 = if (prefs.contains("checkbox_2"))
            prefs.getBoolean("checkbox_2", false).toString()
        else
            getString(R.string.not_set)
        val et1 = prefs.getString("edittext_1", null)
            ?: getString(R.string.not_set)
        textview.text = getString(R.string.template, cb1, cb2, et1)
    }
}