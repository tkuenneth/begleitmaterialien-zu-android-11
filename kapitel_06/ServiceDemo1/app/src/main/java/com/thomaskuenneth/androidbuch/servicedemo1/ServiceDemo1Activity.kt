package com.thomaskuenneth.androidbuch.servicedemo1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class ServiceDemo1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun handleButtonClicked(view: View) {
        val intent = Intent(this, DemoService::class.java)
        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        //    startForegroundService(intent)
        //} else {
            startService(intent)
        //}
        finish()
    }
}