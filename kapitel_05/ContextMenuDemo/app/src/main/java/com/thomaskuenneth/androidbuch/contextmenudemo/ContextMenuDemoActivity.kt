package com.thomaskuenneth.androidbuch.contextmenudemo

import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ContextMenuDemoActivity : AppCompatActivity() {

    private lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        registerForContextMenu(button)
        tv = findViewById(R.id.textview)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View,
        menuInfo: ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.contextmenu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item1 -> {
                tv.text = item.title
                true
            }
            R.id.item2 -> {
                tv.text = item.title
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}