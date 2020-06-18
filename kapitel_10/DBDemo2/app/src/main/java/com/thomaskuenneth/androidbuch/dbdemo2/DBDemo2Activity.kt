package com.thomaskuenneth.androidbuch.dbdemo2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class DBDemo2Activity : AppCompatActivity() {
    private lateinit var openHelper: DBDemo1OpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fine.setOnClickListener { imageButtonClicked(MOOD_FINE) }
        ok.setOnClickListener { imageButtonClicked(MOOD_OK) }
        bad.setOnClickListener { imageButtonClicked(MOOD_BAD) }
        openHelper = DBDemo1OpenHelper(this)
    }

    override fun onPause() {
        super.onPause()
        openHelper.close()
    }

    private fun imageButtonClicked(mood: Int) {
        openHelper.insert(mood, System.currentTimeMillis())
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT)
            .show()
    }
}