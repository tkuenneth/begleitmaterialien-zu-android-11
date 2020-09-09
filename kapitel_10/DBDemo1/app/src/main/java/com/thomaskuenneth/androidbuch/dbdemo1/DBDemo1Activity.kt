package com.thomaskuenneth.androidbuch.dbdemo1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class DBDemo1Activity : AppCompatActivity() {
    private lateinit var openHelper: DBDemo1OpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fine.setOnClickListener { imageButtonClicked(MOOD_FINE) }
        ok.setOnClickListener { imageButtonClicked(MOOD_OK) }
        bad.setOnClickListener { imageButtonClicked(MOOD_BAD) }
    }

    override fun onPause() {
        super.onPause()
        openHelper.close()
    }

    override fun onResume() {
        super. onResume()
        openHelper = DBDemo1OpenHelper(this)
    }

    private fun imageButtonClicked(mood: Int) {
        openHelper.insert(mood, System.currentTimeMillis())
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT)
            .show()
    }
}