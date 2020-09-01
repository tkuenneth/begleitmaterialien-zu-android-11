package com.thomaskuenneth.androidbuch.servicedemo2

import android.content.*
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.thomaskuenneth.androidbuch.servicedemo2.LocalService.LocalBinder
import kotlinx.android.synthetic.main.activity_main.*

class ServiceDemo2Activity : AppCompatActivity() {

    private var service: LocalService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName,
            service: IBinder
        ) {
            val binder = service as LocalBinder
            this@ServiceDemo2Activity.service = binder.service
        }

        override fun onServiceDisconnected(name: ComponentName) {
            this@ServiceDemo2Activity.service = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            service?.let {
                try {
                    val n =
                        edittext.text.toString().toInt()
                    val fak = service?.fakultaet(n)
                    textview.text = getString(
                        R.string.template,
                        n, fak
                    )
                } catch (e: NumberFormatException) {
                    textview.setText(R.string.info)
                }
            }
        }
        edittext.setOnEditorActionListener { _: TextView?,
                                             _: Int, _: KeyEvent? ->
            button.performClick()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, LocalService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        service?.let {
            unbindService(connection)
            service = null
        }
    }
}