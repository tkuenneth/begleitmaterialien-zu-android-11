package com.thomaskuenneth.androidbuch.servicedemo1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

private val TAG = DemoService::class.simpleName

class DemoService : Service() {

    private var shouldBeRunning = false

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind()")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
        shouldBeRunning = true
        Thread {
            while (shouldBeRunning) {
                Log.d(TAG, Date().toString())
                try {
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "Thread.sleep()", e)
                    shouldBeRunning = false
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
        shouldBeRunning = false
    }
}