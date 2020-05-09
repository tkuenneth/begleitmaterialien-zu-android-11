package com.thomaskuenneth.androidbuch.snackbardemo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class SnackbarDemoActivity : AppCompatActivity() {

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        val info = findViewById<TextView>(R.id.info)
        button.setOnClickListener {
            val snackbar = Snackbar.make(button, R.string.info, Snackbar.LENGTH_LONG)
            snackbar.setAction(R.string.action) {
                info.text = getString(R.string.template, ++count)
            }
            snackbar.show()
        }
    }
}