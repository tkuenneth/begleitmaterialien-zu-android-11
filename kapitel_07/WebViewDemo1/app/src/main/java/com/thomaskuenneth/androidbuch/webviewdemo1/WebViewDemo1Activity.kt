package com.thomaskuenneth.androidbuch.webviewdemo1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class WebViewDemo1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webview.loadUrl("https://www.rheinwerk-verlag.de")
        // Webseite mit einem Intent anzeigen
        buttonIntent.setOnClickListener {
            val uri = Uri.parse("https://www.rheinwerk-verlag.de/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        // URL-Kodierung
        buttonHtml.setOnClickListener {
            val html1 = "<html><body><p>Hallo Android</p></body></html>"
            webview.loadData(html1, "text/html", null)
        }
        // Base64-Kodierung
        buttonBase64.setOnClickListener {
            val html2 = "<html><body><p>Hallo Welt</p></body></html>"
            val base64 = Base64.encodeToString(html2.toByteArray(), Base64.DEFAULT)
            webview.loadData(base64, "text/html", "base64")
        }
    }
}