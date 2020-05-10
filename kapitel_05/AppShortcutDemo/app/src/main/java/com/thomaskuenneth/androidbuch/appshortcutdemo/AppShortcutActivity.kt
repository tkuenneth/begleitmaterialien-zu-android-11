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

private const val ACTION = "com.thomaskuenneth.androidbuch.appshortcutdemo.AppShortcut"

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
        if (intent != null && ACTION == intent.action) {
            val uri = intent.data
            message = uri?.toString() ?: getString(R.string.txt_static)
        }
        // dynamischer Shortcut
        val intent = Intent(this, AppShortcutActivity::class.java)
        intent.action = ACTION
        intent.data = Uri.parse("https://www.rheinwerk-verlag.de/")
        val mgr = getSystemService(ShortcutManager::class.java)
        if (mgr != null) {
            val shortcut = ShortcutInfo.Builder(this,
                    "dynamic1")
                    .setShortLabel(getString(R.string.dynamic_shortcut))
                    .setIcon(Icon.createWithResource(this,
                            R.drawable.ic_cloud))
                    .setIntent(intent)
                    .build()
            mgr.dynamicShortcuts = Collections.singletonList(shortcut)
        }
        textview.text = message
    }
}