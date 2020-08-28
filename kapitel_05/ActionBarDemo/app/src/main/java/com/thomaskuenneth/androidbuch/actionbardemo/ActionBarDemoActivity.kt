package com.thomaskuenneth.androidbuch.actionbardemo

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActionBarDemoActivity : AppCompatActivity() {

    private lateinit var textview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textview = findViewById(R.id.textview)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            Toast.makeText(this, R.string.app_name,
                Toast.LENGTH_LONG).show()
        } else {
            textview.text = item.title
        }
        return true
    }
}