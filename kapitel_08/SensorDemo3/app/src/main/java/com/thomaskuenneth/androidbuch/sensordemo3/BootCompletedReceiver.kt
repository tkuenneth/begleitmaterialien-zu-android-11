package com.thomaskuenneth.androidbuch.sensordemo3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            updateSharedPrefs(context, 0)
        }
    }
}