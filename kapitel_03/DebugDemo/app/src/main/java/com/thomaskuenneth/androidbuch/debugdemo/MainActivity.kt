package com.thomaskuenneth.androidbuch.debugdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.lang.NullPointerException

val TAG = MainActivity::class.simpleName

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fakultaet = 1
        println("0! = $fakultaet")
        for (i in 1..5) {
            fakultaet *= i
            println("$i! = $fakultaet")
        }

        Log.v(TAG, "ausführliche Protokollierung, nicht in Produktion verwenden")
        Log.d(TAG, "Debug-Ausgaben")
        Log.i(TAG, "Informationen")
        Log.w(TAG, "Warnungen")
        Log.e(TAG, "Fehler")

        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "noch eine Debug-Ausgabe")
        }

        val s: String? = null
        try {
            Log.d(TAG, "s ist ${s!!.length} Zeichen lang")
        } catch (e: NullPointerException) {
            Log.e(TAG, "Es ist ein Fehler aufgetreten.", e)
        } finally {
            Log.d(TAG, "s ist $s")
        }

        finish()
    }
}