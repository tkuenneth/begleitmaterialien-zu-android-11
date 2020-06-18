package com.thomaskuenneth.androidbuch.dbdemo2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.ListFragment

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()
    }
}