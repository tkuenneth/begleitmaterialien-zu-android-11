package com.thomaskuenneth.androidbuch.servicedemo3_service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

const val MsgFakultaetIn = 1
const val MsgFakultaetOut = 2
private val TAG = RemoteService::class.simpleName
class RemoteService : Service() {

    private lateinit var mMessenger: Messenger

    override fun onBind(intent: Intent?): IBinder? {
        mMessenger = Messenger(IncomingHandler())
        return mMessenger.binder
    }

    private class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MsgFakultaetIn -> {
                    val n = msg.arg1
                    Log.d(TAG, "Eingabe: $n")
                    val fak = fakultaet(n)
                    val m = msg.replyTo
                    val msg2 = Message.obtain(
                        null,
                        MsgFakultaetOut, n, fak
                    )
                    try {
                        m.send(msg2)
                    } catch (e: RemoteException) {
                        Log.e(TAG, "send()", e)
                    }
                }
                else -> super.handleMessage(msg)
            }
        }

        private fun fakultaet(n: Int): Int {
            return if (n <= 0) {
                1
            } else n * fakultaet(n - 1)
        }
    }
}