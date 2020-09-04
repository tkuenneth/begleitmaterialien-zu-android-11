package com.thomaskuenneth.androidbuch.broadcastreceiverdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.DateFormat
import java.util.*

private val ID = 42
private val CHANNEL_ID = "BCR_01"
class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            // Benachrichtigung zusammenbauen
            val msg = DateFormat.getDateTimeInstance().format(Date())
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            builder.setSmallIcon(
                R.mipmap.ic_launcher
            ).setContentTitle(
                context.getString(R.string.app_name)
            ).setContentText(msg).setWhen(System.currentTimeMillis())
            val notification = builder.build()
            val manager = NotificationManagerCompat.from(context)
            // Kanal anlegen
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
            // anzeigen
            manager.notify(ID, notification)
        }
    }
}