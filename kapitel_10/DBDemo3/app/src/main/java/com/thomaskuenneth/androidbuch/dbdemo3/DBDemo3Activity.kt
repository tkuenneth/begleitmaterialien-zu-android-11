package com.thomaskuenneth.androidbuch.dbdemo3

import android.content.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thomaskuenneth.androidbuch.dbdemo2.*
import kotlinx.android.synthetic.main.activity_main.*

class DBDemo3Activity : AppCompatActivity() {
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
    }

    private fun imageButtonClicked(mood: Int) {
        val values = ContentValues()
        values.put(MOOD_MOOD, mood)
        values.put(
            MOOD_TIME,
            System.currentTimeMillis()
        )
        contentResolver
            .insert(CONTENT_URI, values)
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT)
            .show()
    }
}