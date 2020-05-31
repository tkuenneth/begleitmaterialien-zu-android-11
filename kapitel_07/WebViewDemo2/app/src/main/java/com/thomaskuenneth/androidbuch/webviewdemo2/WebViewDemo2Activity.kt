package com.thomaskuenneth.androidbuch.webviewdemo2

import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class WebViewDemo2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prev.setOnClickListener { webview.goBack() }
        next.setOnClickListener { webview.goForward() }
        edittext.setOnEditorActionListener { v, _, _ ->
            webview.loadUrl(v.text.toString())
            true
        }
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView,
                                                  r: WebResourceRequest): Boolean {
                view.loadUrl(r.url.toString())
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                updateNavBar()
            }

            override fun onReceivedError(view: WebView,
                                         request: WebResourceRequest,
                                         error: WebResourceError) {
                updateNavBar()
            }
        }
        webview.settings.builtInZoomControls = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.useWideViewPort = true
        if (savedInstanceState != null) {
            webview.restoreState(savedInstanceState)
        } else {
            webview.loadUrl("https://www.rheinwerk-verlag.de/")
        }
        webview.requestFocus()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webview.saveState(outState)
    }

    private fun updateNavBar() {
        prev.isEnabled = webview.canGoBack()
        next.isEnabled = webview.canGoForward()
        edittext.setText(webview.url)
    }
}