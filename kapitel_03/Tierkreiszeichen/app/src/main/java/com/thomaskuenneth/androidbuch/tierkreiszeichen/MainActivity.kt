package com.thomaskuenneth.androidbuch.tierkreiszeichen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: TierkreiszeichenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = TierkreiszeichenAdapter(this)
        val liste = findViewById<ListView>(R.id.liste)
        liste.adapter = adapter
        liste.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val zeichen = adapter.getItem(position) as Tierkreiszeichen
            val url = getString(R.string.wikipedia_url, zeichen.getName(this))
            // eine Webseite anzeigen
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse(url)
            )
            startActivity(viewIntent)
        }
    }
}