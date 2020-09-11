package com.thomaskuenneth.androidbuch.kamerademo2

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_WRITE_EXTERNAL_STORAGE = 123
private const val REQUEST_IMAGE_CAPTURE = 1
private val TAG = KameraDemo2Activity::class.simpleName
class KameraDemo2Activity : AppCompatActivity() {
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { startCamera() }
    }

    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE)
            button.isEnabled = false
        } else {
            button.isEnabled = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode ==
            REQUEST_WRITE_EXTERNAL_STORAGE &&
            grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED) {
            button.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                val source = ImageDecoder.createSource(this.contentResolver,
                    imageUri)
                val bitmapSource = ImageDecoder.decodeBitmap(source)
                // Größe des aufgenommenen Bildes
                val wSource = bitmapSource.width
                val hSource = bitmapSource.height
                // auf eine Höhe von maximal 300 Pixel skalieren
                val hDesti = if (hSource > 300) 300 else hSource
                val wDesti = (wSource.toFloat() / hSource.toFloat()
                        * hDesti.toFloat()).toInt()
                val bitmapDesti = Bitmap.createScaledBitmap(bitmapSource,
                    wDesti, hDesti, false)
                imageView.setImageBitmap(bitmapDesti)
            } else {
                val rowsDeleted = contentResolver.delete(imageUri,
                    null, null)
                Log.d(TAG, "$rowsDeleted rows deleted")
            }
        }
    }

    private fun startCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,
            getString(R.string.app_name))
        values.put(MediaStore.Images.Media.DESCRIPTION,
            getString(R.string.descr))
        values.put(MediaStore.Images.Media.MIME_TYPE,
            "image/jpeg")
        contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values)?.let {
            imageUri = it
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }
}