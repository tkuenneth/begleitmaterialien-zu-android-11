package com.thomaskuenneth.androidbuch.servicedemo2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.thomaskuenneth.androidbuch.servicedemo2.LocalService.LocalBinder

class ServiceDemo2Activity : AppCompatActivity() {

    private var mService: LocalService? = null

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName,
            service: IBinder
        ) {
            val binder = service as LocalBinder
            mService = binder.service
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textview = findViewById<TextView>(R.id.textview)
        val edittext = findViewById<EditText>(R.id.edittext)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            if (mService != null) {
                try {
                    val n =
                        edittext.text.toString().toInt()
                    val fak = mService?.fakultaet(n)
                    textview.text = getString(
                        R.string.template,
                        n, fak
                    )
                } catch (e: NumberFormatException) {
                    textview.setText(R.string.info)
                }
            }
        }
        edittext.setOnEditorActionListener { _: TextView?, _: Int, _: KeyEvent? ->
            button.performClick()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, LocalService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (mService != null) {
            unbindService(mConnection)
            mService = null
        }
    }
}