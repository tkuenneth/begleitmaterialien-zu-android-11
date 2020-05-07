package com.thomaskuenneth.androidbuch.broadcastreceiverdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.DateFormat
import java.util.*

class BootCompletedReceiver : BroadcastReceiver() {

    private val d = 42
    private val channelId = "BCR_01"

    override fun onReceive(context: Context?, intent: Intent?) {
        if ((intent == null) || (context == null)) return
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            // Benachrichtigung zusammenbauen
            val msg = DateFormat.getDateTimeInstance().format(Date())
            val builder = NotificationCompat.Builder(context, channelId)
            builder.setSmallIcon(
                R.mipmap.ic_launcher
            ).setContentTitle(
                context.getString(R.string.app_name)
            ).setContentText(msg).setWhen(System.currentTimeMillis())
            val notification = builder.build()
            val manager = NotificationManagerCompat.from(context)
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