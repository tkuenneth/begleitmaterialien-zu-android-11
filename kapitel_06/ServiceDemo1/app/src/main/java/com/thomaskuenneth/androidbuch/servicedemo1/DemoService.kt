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

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val channelId = "channelId_1234"
//            val channel = android.app.NotificationChannel(
//                channelId,
//                getString(R.string.app_name),
//                android.app.NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val nm =
//                getSystemService(android.app.NotificationManager::class.java)
//            if (nm != null) {
//                nm.createNotificationChannel(channel)
//                val b = android.app.Notification.Builder(
//                    this,
//                    channelId
//                )
//                b.setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText(getString(R.string.app_name))
//                startForeground(0x1234, b.build())
//            }
//        }
//        return super.onStartCommand(intent, flags, startId)
//    }
}