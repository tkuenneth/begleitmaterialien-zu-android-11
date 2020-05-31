package com.thomaskuenneth.androidbuch.webviewdemo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class WebViewDemo3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webview.settings.javaScriptEnabled = true
        webview.loadUrl("file:/android_asset/test1.html")
    }
}