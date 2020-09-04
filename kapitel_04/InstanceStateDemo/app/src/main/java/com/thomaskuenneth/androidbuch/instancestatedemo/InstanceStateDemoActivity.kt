package com.thomaskuenneth.androidbuch.instancestatedemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

private val TAG = InstanceStateDemoActivity::class.simpleName
class InstanceStateDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            Log.d(TAG, "savedInstanceState war null")
        } else {
            val time = System.currentTimeMillis() - savedInstanceState
                    .getLong(TAG)
            Log.d(TAG, "wurde vor $time Millisekunden beendet")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(TAG, System.currentTimeMillis())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState()")
    }
}