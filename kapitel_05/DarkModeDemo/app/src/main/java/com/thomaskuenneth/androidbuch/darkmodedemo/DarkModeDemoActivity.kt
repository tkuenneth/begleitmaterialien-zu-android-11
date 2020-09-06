package com.thomaskuenneth.androidbuch.darkmodedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

class DarkModeDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}