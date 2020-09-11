package com.thomaskuenneth.androidbuch.gallerydemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity

private const val REQUEST_GALLERY_PICK = 1
class GalleryDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY_PICK)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GALLERY_PICK) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val intentView = Intent(Intent.ACTION_VIEW, it.data)
                    startActivity(intentView)
                }
            } else {
                finish()
            }
        }
    }
}