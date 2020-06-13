package com.thomaskuenneth.androidbuch.druckdemo1

import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

private val TAG = DruckDemo1Activity::class.simpleName

class DruckDemo1Activity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // WebView für den Druck instanziieren
        webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView,
                                                  request: WebResourceRequest): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                // PrintManager-Instanz ermitteln
                val printManager = getSystemService(PrintManager::class.java)
                if (printManager != null) {
                    // Der Adapter stellt den Dokumentinhalt bereit
                    val printAdapter = webView.createPrintDocumentAdapter("Dokumentname")
                    // Druckauftrag erstellen und übergeben
                    val jobName = getString(R.string.app_name) + " Dokument"
                    val attributes = PrintAttributes.Builder().build()
                    val printJob: PrintJob = printManager.print(jobName,
                            printAdapter,
                            attributes)
                    Log.d(TAG, printJob.info.toString())
                }
            }
        }
        val htmlDocument = """
            <html><body>
            <h1>Hallo Android</h1>
            <p><img src="ic_launcher.png" />
            <br />Ein Test</p>
            </body></html>
        """.trimIndent()
        webView.loadDataWithBaseURL("file:///android_asset/",
                htmlDocument, "text/html", "UTF-8", null)
    }
}