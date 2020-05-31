package com.thomaskuenneth.androidbuch.webviewdemo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.webkit.WebResourceRequest
//import android.webkit.WebView
//import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*

class WebViewDemo3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webview.settings.javaScriptEnabled = true
//        webview.addJavascriptInterface(WebAppInterface(this), "Android")
//        val client = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(
//                view: WebView,
//                request: WebResourceRequest
//            ): Boolean {
//                return false
//            }
//        }
//        webview.webViewClient = client
        webview.loadUrl("file:/android_asset/test1.html")
//        webview.loadUrl("file:///android_asset/test2.html")
    }
}