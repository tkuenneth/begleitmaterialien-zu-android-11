package com.thomaskuenneth.androidbuch.activitylifecycledemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private val TAG = ActivityLifecycleDemoActivity::class.simpleName
private var zaehler = 1
class ActivityLifecycleDemoActivity : AppCompatActivity() {

    private var lokalerZaehler = zaehler++

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate")
        setContentView(R.layout.activity_main)
        val tv: TextView = findViewById(R.id.textview)
        tv.text = getString(R.string.msg, lokalerZaehler)
        val buttonNew: Button = findViewById(R.id.id_new)
        buttonNew.setOnClickListener {
            val i = Intent(this,
                ActivityLifecycleDemoActivity::class.java)
            startActivity(i)
        }
        val buttonFinish: Button = findViewById(R.id.id_finish)
        buttonFinish.setOnClickListener { finish() }
    }

    override fun onStart() {
        super.onStart()
        log("onStart")
    }

    override fun onRestart() {
        super.onRestart()
        log("onRestart")
    }

    override fun onResume() {
        super.onResume()
        log("onResume")
    }

    override fun onPause() {
        super.onPause()
        log("onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(methodName: String) {
        Log.d(TAG, "$methodName() #$lokalerZaehler")
    }
}