package com.thomaskuenneth.androidbuch.toastdemo

import android.os.*
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.Callback
import androidx.appcompat.app.AppCompatActivity

class ToastDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val toast = Toast.makeText(
                this,
                R.string.app_name,
                Toast.LENGTH_LONG
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                toast.addCallback(object : Callback() {
                    override fun onToastHidden() {
                        button.isEnabled = true
                    }

                    override fun onToastShown() {
                        button.isEnabled = false
                    }
                })
            }
            toast.show()
        }
        button.isEnabled = true
    }
}