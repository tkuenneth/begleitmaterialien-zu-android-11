package com.thomaskuenneth.androidbuch.notificationdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

class NotificationDemoActivity : AppCompatActivity() {

    private val notificationId = 42
    private val channelId = "channel01"
    private val resultKey = "resultKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            createAndSendNotification()
            finish()
        }
        // Wurde ein Intent empfangen?
        if (intent != null) {
            // Dann verarbeiten
            val text = getMessageText(intent)
            if (text != null) {
                val textview = findViewById<TextView>(R.id.textview)
                textview.text = text
                NotificationManagerCompat.from(this).cancel(notificationId)
            }
        }
    }

    private fun createAndSendNotification() {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, NotificationDemoActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            // BigTextStyle
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(getString(R.string.notification_text))
            )
        // Aktion mit PendingIntent
        val actionBuilder = NotificationCompat.Action.Builder(
            R.mipmap.ic_launcher,
            "Eine Aktion",
            pendingIntent
        )
        // Eingaben machen
        val remoteInput = RemoteInput.Builder(resultKey)
            .setLabel(getString(R.string.reply))
            .setChoices(resources.getStringArray(R.array.choices))
            .build()
        actionBuilder.addRemoteInput(remoteInput)
        builder.addAction(actionBuilder.build())
        // Kanal anlegen und Notification anzeigen
        val manager = NotificationManagerCompat.from(this)
        val channel = NotificationChannel(
            channelId,
            getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)
        manager.notify(
            notificationId,
            builder.build()
        )
    }

    private fun getMessageText(intent: Intent): CharSequence? {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        return remoteInput?.getCharSequence(resultKey)
    }
}