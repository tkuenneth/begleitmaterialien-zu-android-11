package com.thomaskuenneth.androidbuch.servicedemo2

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class LocalService : Service() {
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        val service = this@LocalService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun fakultaet(n: Int): Int {
        return if (n <= 0) {
            1
        } else n * fakultaet(n - 1)
    }
}