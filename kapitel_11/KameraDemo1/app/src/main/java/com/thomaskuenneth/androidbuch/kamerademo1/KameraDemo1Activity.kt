package com.thomaskuenneth.androidbuch.kamerademo1

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class KameraDemo1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        foto.setOnClickListener {
            val intent = Intent(
                MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
            startActivity(intent)
        }
        video.setOnClickListener {
            val intent = Intent(
                MediaStore.INTENT_ACTION_VIDEO_CAMERA)
            startActivity(intent)
        }
    }
}