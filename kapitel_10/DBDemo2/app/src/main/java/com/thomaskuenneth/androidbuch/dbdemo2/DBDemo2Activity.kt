package com.thomaskuenneth.androidbuch.dbdemo2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class DBDemo2Activity : AppCompatActivity() {
    private lateinit var openHelper: DBDemo2OpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fine.setOnClickListener { imageButtonClicked(MOOD_FINE) }
        ok.setOnClickListener { imageButtonClicked(MOOD_OK) }
        bad.setOnClickListener { imageButtonClicked(MOOD_BAD) }
        history.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        openHelper = DBDemo2OpenHelper(this)
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