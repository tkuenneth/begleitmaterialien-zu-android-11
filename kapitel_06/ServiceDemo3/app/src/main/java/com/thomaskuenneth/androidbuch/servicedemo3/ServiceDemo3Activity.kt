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

const val MSG_FACTORIAL_IN = 1
const val MSG_FACTORIAL_OUT = 2
private const val PACKAGE =
    "com.thomaskuenneth.androidbuch.servicedemo3_service"
private val TAG = ServiceDemo3Activity::class.simpleName
class ServiceDemo3Activity : AppCompatActivity() {

    private var service: Messenger? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            this@ServiceDemo3Activity.service = Messenger(service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            service = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textview = findViewById<TextView>(R.id.textview)
        val edittext = findViewById<EditText>(R.id.edittext)
        val button = findViewById<Button>(R.id.button)
        val messenger = Messenger(IncomingHandler(this, textview))
        button.setOnClickListener {
            service?.let {
                try {
                    val n = edittext.text.toString().toInt()
                    val msg = Message.obtain(null,
                        MSG_FACTORIAL_IN, n, 0)
                    msg.replyTo = messenger
                    it.send(msg)
                } catch (e: NumberFormatException) {
                    textview.setText(R.string.info)
                } catch (e: RemoteException) {
                    Log.d(TAG, "send()", e)
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
        val componentName = ComponentName(PACKAGE,
            "${PACKAGE}.RemoteService")
        val intent = Intent()
        intent.component = componentName
        if (!bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            Log.d(TAG, "bindService() nicht erfolgreich")
            service = null
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        service?.let {
            unbindService(connection)
            service = null
        }
    }

    private class IncomingHandler(val context: Context,
                                  val tv: TextView)
        : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_FACTORIAL_OUT -> {
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