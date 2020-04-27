package com.thomaskuenneth.androidbuch.broadcastreceiverdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.text.DateFormat
import java.util.*

class BootCompletedReceiver : BroadcastReceiver() {

    private val d = 42
    private val channelId = "BCR_01"

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            // Benachrichtigung zusammenbauen
            val msg = DateFormat.getDateTimeInstance().format(Date())
            val builder = Notification.Builder(context, channelId)
            builder.setSmallIcon(
                R.mipmap.ic_launcher
            ).setContentTitle(
                context.getString(R.string.app_name)
            ).setContentText(msg).setWhen(System.currentTimeMillis())
            val notification = builder.build()
            val manager = context.getSystemService(NotificationManager::class.java)
            if (manager != null) {
                // Kanal anlegen
                val channel = NotificationChannel(
                    channelId,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channel)
                // anzeigen
                manager.notify(d, notification)
            }
        }
    }
}