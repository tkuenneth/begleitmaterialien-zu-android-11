package com.thomaskuenneth.androidbuch.threaddemo2

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

private val TAG = ThreadDemo2Activity::class.simpleName
class ThreadDemo2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, Thread.currentThread().name)
        val tv = findViewById<TextView>(R.id.textview)
        val checkbox = findViewById<CheckBox>(R.id.checkbox)
        checkbox.setOnCheckedChangeListener { _, isChecked: Boolean -> tv.text = isChecked.toString() }
        checkbox.isChecked = true
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // --- Beginn Experimente ---

            // ursprüngliche Fassung
//            tv.text = getString(R.string.begin)
//            if (checkbox.isChecked) {
//                try {
//                    Thread.sleep(3500)
//                } catch (e: InterruptedException) {
//                    Log.e(TAG, "sleep()", e)
//                }
//            } else {
//                while (true) { }
//            }
//            tv.text = getString(R.string.end)

            // erste alternative Fassung
//            Thread {
//                try {
//                    Thread.sleep(10000)
//                } catch (e: InterruptedException) {
//                    Log.e(TAG, "sleep()", e)
//                }
//            }.start()

            // fehlerhafte Version
//            Thread {
//                tv.text = getString(R.string.begin)
//                try {
//                    Thread.sleep(10000)
//                } catch (e: InterruptedException) {
//                    Log.e(TAG, "sleep()", e)
//                }
//                tv.text = getString(R.string.end)
//            }.start()

            // korrekte Version mit Handler
//            val h = android.os.Handler(Looper.getMainLooper())
//            Thread {
//                try {
//                    h.post { tv.text = getString(R.string.begin) }
//                    Thread.sleep(10000)
//                    h.post { tv.text = getString(R.string.end) }
//                } catch (e: InterruptedException) {
//                    Log.e(TAG, "sleep()", e)
//                }
//            }.start()

            // ebenfalls korrekte Version mit runOnUiThread()
            val t = Thread {
                try {
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "sleep()", e)
                }
                runOnUiThread { tv.text = getString(R.string.end) }
            }
            tv.text = getString(R.string.begin)
            t.start()

            // --- Ende Experimente ---
        }
    }
}