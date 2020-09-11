package com.thomaskuenneth.androidbuch.kamerademo3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Size
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

private val TAG = KameraDemo3Activity::class.simpleName
class KameraDemo3Activity : AppCompatActivity() {
    private val requestCamera = 123
    private var camera: CameraDevice? = null
    private var activeSession: CameraCaptureSession? = null

    private lateinit var manager: CameraManager
    private lateinit var cameraId: String
    private lateinit var imageReader: ImageReader
    private lateinit var builderPreview: CaptureRequest.Builder

    private val captureSessionCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            try {
                session.setRepeatingRequest(builderPreview.build(), null, null)
                activeSession = session
            } catch (e: CameraAccessException) {
                Log.e(TAG, "onConfigured()", e)
            }
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.e(TAG, "onConfigureFailed()")
        }
    }

    private val surfaceHolderCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.d(TAG, "surfaceDestroyed()")
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.d(TAG, "surfaceCreated()")
            try {
                openCamera()
            } catch (e: Exception) {
                // SecurityException, CameraAccessException
                Log.e(TAG, "openCamera()", e)
            }
        }

        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int, width: Int,
            height: Int
        ) {
            Log.d(TAG, "surfaceChanged()")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = getSystemService(CameraManager::class.java)
        setContentView(R.layout.activity_main)
        camera = null
    }

    override fun onPause() {
        super.onPause()
        surfaceview.visibility = View.GONE
        activeSession?.close()
        activeSession = null
        camera?.close()
        camera = null
        surfaceview.holder.removeCallback(surfaceHolderCallback)
    }

    override fun onResume() {
        super.onResume()
        if (checkSelfPermission(Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                requestCamera
            )
        } else {
            configureHolder()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCamera &&
            grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            configureHolder()
        }
    }

    private fun configureHolder() {
        surfaceview.holder.addCallback(surfaceHolderCallback)
        val sizes = findCameraFacingBack()
        if (sizes.isEmpty()) {
            Log.d(TAG, "keine passende Kamera gefunden")
            finish()
        }
        val metrics = resources.displayMetrics
        for (size in sizes) {
            if (size.width > metrics.widthPixels
                || size.height > metrics.heightPixels
            ) {
                continue
            }
            surfaceview.setOnClickListener { takePicture() }
            surfaceview.holder.setFixedSize(size.width, size.height)
            surfaceview.visibility = View.VISIBLE
            imageReader = ImageReader.newInstance(
                size.width, size.height,
                ImageFormat.JPEG, 2
            )
            imageReader.setOnImageAvailableListener(
                {
                    Log.d(TAG, "setOnImageAvailableListener()")
                    val image = imageReader.acquireLatestImage()
                    val planes = image.planes
                    val buffer = planes[0].buffer
                    saveJPG(buffer)
                    image.close()
                }, null
            )
            return
        }
        Log.d(TAG, "Zu groß")
        finish()
    }

    // vorhandene Kameras ermitteln und auswählen
    private fun findCameraFacingBack(): Array<Size> {
        cameraId = ""
        try {
            val ids = manager.cameraIdList
            for (id in ids) {
                val cc = manager.getCameraCharacteristics(id)
                Log.d(TAG, "$id: $cc")
                val lensFacing = cc.get(CameraCharacteristics.LENS_FACING)
                if (lensFacing != null &&
                    lensFacing ==
                    CameraCharacteristics.LENS_FACING_BACK
                ) {
                    cameraId = id
                    cc.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                    )?.run {
                        return getOutputSizes(SurfaceHolder::class.java)
                    }
                }
            }
        } catch (e: CameraAccessException) {
            Log.e(TAG, "findCameraFacingBack()", e)
        } catch (e: NullPointerException) {
            Log.e(TAG, "findCameraFacingBack()", e)
        }
        return emptyArray()
    }

    @Throws(SecurityException::class)
    private fun openCamera() {
        manager.openCamera(
            cameraId,
            object : CameraDevice.StateCallback() {
                override fun onOpened(_camera: CameraDevice) {
                    Log.d(TAG, "onOpened()")
                    camera = _camera
                    createPreviewCaptureSession()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    Log.d(TAG, "onDisconnected()")
                }

                override fun onError(
                    camera: CameraDevice,
                    error: Int
                ) {
                    Log.d(TAG, "onError()")
                }
            }, null
        )
    }

    private fun createPreviewCaptureSession() {
        val outputs = mutableListOf<OutputConfiguration>()
        outputs.add(OutputConfiguration(surfaceview.holder.surface))
        outputs.add(OutputConfiguration(imageReader.surface))
        val sessionConfiguration = SessionConfiguration(
            SessionConfiguration.SESSION_REGULAR,
            outputs, mainExecutor, captureSessionCallback
        )
        try {
            camera?.createCaptureRequest(
                CameraDevice.TEMPLATE_PREVIEW
            )?.let {
                builderPreview = it
                it.addTarget(surfaceview.holder.surface)
                camera?.createCaptureSession(sessionConfiguration)
            }
        } catch (e: Exception) {
            Log.e(TAG, "createPreviewCaptureSession()", e)
        }
    }

    private fun takePicture() {
        try {
            val builder = camera?.createCaptureRequest(
                CameraDevice.TEMPLATE_STILL_CAPTURE
            )
            builder?.addTarget(imageReader.surface)
            builder?.build()?.let {
                activeSession?.capture(it, null, null)
            }
        } catch (e: CameraAccessException) {
            Log.e(TAG, "takePicture()", e)
        }
    }

    private fun saveJPG(data: ByteBuffer) {
        getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
            if (it.mkdirs()) {
                Log.d(TAG, "dirs created")
            }
            val f = File(it, "${TAG}_${System.currentTimeMillis()}.jpg")
            try {
                FileOutputStream(f).use { fos ->
                    BufferedOutputStream(fos).use { bos ->
                        while (data.hasRemaining()) {
                            bos.write(data.get().toInt())
                        }
                        Toast.makeText(
                            this, R.string.click,
                            Toast.LENGTH_SHORT
                        ).show()
                        addToMediaProvider(f)
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "saveJPG()", e)
            }
        }
    }

    private fun addToMediaProvider(f: File) {
        MediaScannerConnection.scanFile(
            this,
            arrayOf(f.toString()),
            arrayOf("image/jpeg")
        ) { _, uri ->
            val i = Intent(
                Intent.ACTION_VIEW,
                uri
            )
            startActivity(i)
        }
    }
}