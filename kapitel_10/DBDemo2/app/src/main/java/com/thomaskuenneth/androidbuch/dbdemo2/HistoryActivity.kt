package com.thomaskuenneth.androidbuch.dbdemo2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, HistoryFragment())
            .commit()
    }
}