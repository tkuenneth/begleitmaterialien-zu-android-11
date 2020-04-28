package com.thomaskuenneth.androidbuch.fragmentdemo1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FragmentDemo1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<TextView>(R.id.textview)
        view.text = getString(R.string.text2)
    }
}