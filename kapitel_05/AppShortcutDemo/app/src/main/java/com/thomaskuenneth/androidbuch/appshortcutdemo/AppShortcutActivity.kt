package com.thomaskuenneth.androidbuch.appshortcutdemo

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

private const val ACTION =
    "com.thomaskuenneth.androidbuch.appshortcutdemo.AppShortcut"
private const val ID = "dynamic1"
class AppShortcutActivity : AppCompatActivity() {

    private lateinit var textview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textview = findViewById(R.id.textview)
    }

    override fun onStart() {
        super.onStart()
        var message = getString(R.string.app_name)
        if (ACTION == intent?.action) {
            val uri = intent.data
            message = uri?.toString() ?: getString(R.string.txt_static)
        }
        // dynamischer Shortcut
        val shortcutIntent = Intent(this, AppShortcutActivity::class.java)
        shortcutIntent.action = ACTION
        shortcutIntent.data = Uri.parse("https://www.rheinwerk-verlag.de/")
        getSystemService(ShortcutManager::class.java)?.let {
            val shortcut = ShortcutInfo.Builder(this,
                ID)
                .setShortLabel(getString(R.string.dynamic_shortcut))
                .setIcon(Icon.createWithResource(this,
                    R.drawable.ic_cloud))
                .setIntent(shortcutIntent)
                .build()
            it.dynamicShortcuts = Collections.singletonList(shortcut)
        }
        textview.text = message
    }
}