package com.thomaskuenneth.androidbuch.webviewdemo3

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class WebAppInterface(private val context: Context) {
    @JavascriptInterface
    fun getHeadline(): String {
        return context.getString(R.string.headline)
    }

    @JavascriptInterface
    fun message(m: String) {
        val toast = Toast.makeText(context, m, Toast.LENGTH_LONG)
        toast.show()
    }
}