package com.thomaskuenneth.androidbuch.notificationdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

private const val NOTIFICATION_ID = 42
private const val CHANNEL_ID = "channel01"
private const val RESULT_KEY = "resultKey"
class NotificationDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            createAndSendNotification()
            finish()
        }
        // Wurde ein Intent empfangen?
        intent?.let {
            // Dann verarbeiten
            getMessageText(it)?.let { text ->
                findViewById<TextView>(R.id.textview).text = text
                NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
            }
        }
    }

    private fun createAndSendNotification() {
        val pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, NotificationDemoActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
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
            pendingIntent)
        // Eingaben machen
        val remoteInput = RemoteInput.Builder(RESULT_KEY)
            .setLabel(getString(R.string.reply))
            .setChoices(resources.getStringArray(R.array.choices))
            .build()
        actionBuilder.addRemoteInput(remoteInput)
        builder.addAction(actionBuilder.build())
        val manager = NotificationManagerCompat.from(this)
        val channel = NotificationChannel(CHANNEL_ID,
            getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)
        manager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun getMessageText(intent: Intent): CharSequence? =
        RemoteInput.getResultsFromIntent(intent)?.getCharSequence(RESULT_KEY)
}