package com.thomaskuenneth.androidbuch.multiwindowdemo

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private val TAG = MainActivity::class.simpleName
class MainActivity : AppCompatActivity() {

    private val sb = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            val intent = Intent(this, ChildActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        animation.isEnabled = false
        Log.d(TAG, "onStop()")
    }

    override fun onStart() {
        super.onStart()
        updateTextView()
        animation.isEnabled = true
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
        textview.text = sb.toString()
    }
}