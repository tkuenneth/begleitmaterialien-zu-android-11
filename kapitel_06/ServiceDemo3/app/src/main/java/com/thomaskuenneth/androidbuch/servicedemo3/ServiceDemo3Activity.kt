package com.thomaskuenneth.androidbuch.servicedemo3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

const val MsgFakultaetIn = 1
const val MsgFakultaetOut = 2

private val TAG = ServiceDemo3Activity::class.simpleName

class ServiceDemo3Activity : AppCompatActivity() {

    private var mService: Messenger? = null

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            mService = Messenger(service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textview = findViewById<TextView>(R.id.textview)
        val edittext = findViewById<EditText>(R.id.edittext)
        val button = findViewById<Button>(R.id.button)
        val mMessenger = Messenger(IncomingHandler(this, textview))
        button.setOnClickListener {
            if (mService != null) {
                try {
                    val n =
                            edittext.text.toString().toInt()
                    val msg = Message.obtain(null,
                            MsgFakultaetIn,
                            n, 0)
                    msg.replyTo = mMessenger
                    mService?.send(msg)
                } catch (e: NumberFormatException) {
                    textview.setText(R.string.info)
                } catch (e: RemoteException) {
                    Log.d(TAG, "send()", e)
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
        val componentName = ComponentName(
                "com.thomaskuenneth.androidbuch.servicedemo3_service",
                "com.thomaskuenneth.androidbuch.servicedemo3_service.RemoteService")
        val intent = Intent()
        intent.component = componentName
        if (!bindService(intent, mConnection, Context.BIND_AUTO_CREATE)) {
            Log.d(TAG, "bindService() nicht erfolgreich")
            mService = null
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mService != null) {
            unbindService(mConnection)
            mService = null
        }
    }

    private class IncomingHandler(val context: Context, val tv: TextView) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MsgFakultaetOut -> {
                    val n = msg.arg1
                    val fakultaet = msg.arg2
                    Log.d(TAG, "Fakultaet: $fakultaet")
                    tv.text = context.getString(R.string.template,
                            n, fakultaet)
                }
                else -> super.handleMessage(msg)
            }
        }
    }
}