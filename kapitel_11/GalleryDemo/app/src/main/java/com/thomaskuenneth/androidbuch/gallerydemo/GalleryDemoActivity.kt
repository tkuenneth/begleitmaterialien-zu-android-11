package com.thomaskuenneth.androidbuch.gallerydemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity

class GalleryDemoActivity : AppCompatActivity() {
    private val requestGalleryPick = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestGalleryPick)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestGalleryPick) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val uri = it.data
                    val intentView = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intentView)
                }
            } else {
                finish()
            }
        }
    }
}