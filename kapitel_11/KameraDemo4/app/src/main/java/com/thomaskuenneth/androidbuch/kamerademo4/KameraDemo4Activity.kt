package com.thomaskuenneth.androidbuch.kamerademo4

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.VideoCapture.*
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.util.Log

private const val REQUEST_PERMISSIONS = 123
private val TAG = KameraDemo4Activity::class.simpleName
class KameraDemo4Activity : AppCompatActivity() {
    private val requestCameraRecordAudio =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.isEnabled = false
        button.setOnClickListener { toggleRecord() }
        updateButton()
        if (checkPermissions()) {
            preview.post { startCamera() }
        } else {
            requestPermissions(
                requestCameraRecordAudio, REQUEST_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (checkPermissions()) {
                preview.post { startCamera() }
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        for (permission in requestCameraRecordAudio) {
            if (checkSelfPermission(
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @Throws(SecurityException::class)
    private fun startCamera() {
        button.isEnabled = true
        preview.bindToLifecycle(this)
    }

    private fun toggleRecord() {
        isRecording = !isRecording
        updateButton()
        if (isRecording) {
            val dir = File(cacheDir, "videos")
            dir.mkdirs()
            val file = File(dir, "${System.currentTimeMillis()}.mp4")
            preview.startRecording(file, mainExecutor,
                object : OnVideoSavedCallback {
                    override fun onVideoSaved(outputFileResults:
                                              OutputFileResults) {
                        Toast.makeText(
                            this@KameraDemo4Activity,
                            file.absolutePath,
                            Toast.LENGTH_LONG
                        ).show()
                        val uri = FileProvider.getUriForFile(
                            this@KameraDemo4Activity,
                            "com.thomaskuenneth.androidbuch.kamerademo4.fileprovider",
                            file
                        )
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "video/*"
                        intent.putExtra(Intent.EXTRA_STREAM, uri)
                        val chooser = Intent.createChooser(intent,
                            getString(R.string.share))
                        val l = packageManager.queryIntentActivities(
                            chooser,
                            PackageManager.MATCH_DEFAULT_ONLY
                        )
                        for (resolveInfo in l) {
                            val packageName = resolveInfo.activityInfo.packageName
                            grantUriPermission(
                                packageName,
                                uri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                        }
                        startActivity(chooser)
                    }

                    override fun onError(
                        videoCaptureError: Int,
                        message: String,
                        cause: Throwable?
                    ) {
                        Log.e(TAG, message, cause)
                    }
                })
        } else
            preview.stopRecording()
    }

    private fun updateButton() {
        button.text = getString(if (isRecording) R.string.stop
        else R.string.start)
    }
}