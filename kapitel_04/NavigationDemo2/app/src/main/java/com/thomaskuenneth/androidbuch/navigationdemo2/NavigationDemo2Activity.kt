package com.thomaskuenneth.androidbuch.navigationdemo2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationDemo2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textview = findViewById<TextView>(R.id.textview)
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_home -> {
                    textview.text = getString(R.string.home)
                    true
                }
                R.id.page_info -> {
                    textview.text = getString(R.string.info)
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.page_home
    }
}