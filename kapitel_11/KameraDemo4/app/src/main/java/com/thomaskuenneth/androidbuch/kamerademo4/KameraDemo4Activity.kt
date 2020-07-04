package com.thomaskuenneth.androidbuch.kamerademo4

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.VideoCapture
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class KameraDemo4Activity : AppCompatActivity() {

    private val requestPermissions = 123
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
                requestCameraRecordAudio, requestPermissions
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == requestPermissions) {
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
            val file = File(filesDir, "${System.currentTimeMillis()}.mp4")
            preview.startRecording(file, mainExecutor,
                object : VideoCapture.OnVideoSavedCallback {
                    override fun onVideoSaved(file: File) {
                        Toast.makeText(
                            this@KameraDemo4Activity,
                            file.absolutePath,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onError(
                        videoCaptureError: Int,
                        message: String,
                        cause: Throwable?
                    ) {
                    }
                })
        } else
            preview.stopRecording()
    }

    private fun updateButton() {
        button.text = getString(if (isRecording) R.string.stop else R.string.start)
    }
}