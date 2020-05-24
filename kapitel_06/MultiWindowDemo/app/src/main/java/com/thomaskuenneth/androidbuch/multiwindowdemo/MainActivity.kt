package com.thomaskuenneth.androidbuch.multiwindowdemo

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

private val TAG = MainActivity::class.simpleName

class MainActivity : AppCompatActivity() {

    private val sb = StringBuilder()

    private lateinit var tv: TextView
    private lateinit var view: AnimatedNumberView
    private lateinit var bt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.tv)
        view = findViewById(R.id.anim)
        bt = findViewById(R.id.launch)
        bt.setOnClickListener {
            val intent = Intent(this, ChildActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        view.isEnabled = false
        Log.d(TAG, "onStop()")
    }

    override fun onStart() {
        super.onStart()
        updateTextView()
        view.isEnabled = true
        Log.d(TAG, "onStart()")
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        Log.d(
            TAG, "onPictureInPictureModeChanged(): " +
                    isInPictureInPictureMode
        )
    }

    override fun onMultiWindowModeChanged(
        isInMultiWindowMode: Boolean,
        newConfig: Configuration?
    ) {
        Log.d(
            TAG, "onMultiWindowModeChanged(): " +
                    isInMultiWindowMode
        )
    }

    private fun updateTextView() {
        sb.setLength(0)
        sb.append("isInMultiWindowMode(): ")
            .append(isInMultiWindowMode)
            .append("\n")
        sb.append("isInPictureInPictureMode(): ")
            .append(isInPictureInPictureMode)
            .append("\n")
        tv.text = sb.toString()
    }
}