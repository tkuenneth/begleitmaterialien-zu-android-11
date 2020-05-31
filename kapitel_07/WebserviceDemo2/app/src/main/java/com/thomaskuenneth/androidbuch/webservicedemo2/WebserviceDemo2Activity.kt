package com.thomaskuenneth.androidbuch.webservicedemo2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.regex.Pattern

private val TAG = WebserviceDemo2Activity::class.simpleName

class WebserviceDemo2Activity : AppCompatActivity() {
    private val pattern = Pattern.compile(".*charset\\s*=\\s*(.*)$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        input.setOnEditorActionListener { _, _, _ -> button.performClick() }
        button.setOnClickListener {
            Thread {
                val result = talkToServer(input.text.toString())
                runOnUiThread {
                    output.text = result
                }
            }.start()
        }
    }

    private fun talkToServer(name: String): String {
        val sb = StringBuilder()
        val url = URL("http://10.0.2.2:8080/hello")
        try {
            val connection = url.openConnection() as HttpURLConnection
            // Verbindung konfigurieren
            connection.doOutput = true
            connection.requestMethod = "POST"
            val data = name.toByteArray()
            connection.setRequestProperty(
                "Content-Type", "text/plain; charset="
                        + Charset.defaultCharset().name()
            )
            connection.setFixedLengthStreamingMode(data.size)
            // Daten senden
            connection.outputStream.write(data)
            connection.outputStream.flush()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                var charSet = "ISO-8859-1"
                val m = pattern.matcher(connection.contentType)
                if (m.matches()) {
                    charSet = m.group(1) ?: charSet
                }
                InputStreamReader(
                    connection.inputStream, charSet
                ).use {
                    BufferedReader(
                        it
                    ).use { bufferedReader ->
                        bufferedReader.lines().forEach { line ->
                            sb.append(line)
                        }
                    }
                }
            } else {
                Log.d(TAG, "responseCode: ${connection.responseCode}")
            }
            connection.disconnect()
        } catch (tr: Throwable) {
            Log.e(TAG, "Fehler beim Zugriff auf $url", tr)
        }
        return sb.toString()
    }
}