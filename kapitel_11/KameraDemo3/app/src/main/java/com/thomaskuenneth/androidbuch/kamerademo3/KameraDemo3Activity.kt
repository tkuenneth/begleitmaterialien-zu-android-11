package com.thomaskuenneth.androidbuch.kamerademo3

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


private val TAG = KameraDemo3Activity::class.simpleName

class KameraDemo3Activity : AppCompatActivity() {
    private val requestCamera = 123
    private var camera: CameraDevice? = null
    private var activeSession: CameraCaptureSession? = null
    private var builderPreview: CaptureRequest.Builder? = null
    private val captureCallback: CaptureCallback? = null
    private val captureSessionCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            try {
                builderPreview?.build()?.let {
                    session.setRepeatingRequest(it, captureCallback, null)
                    activeSession = session
                }
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
            } catch (e: SecurityException) {
                Log.e(TAG, "openCamera()", e)
            } catch (e: CameraAccessException) {
                Log.e(TAG, "openCamera()", e)
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder,
                                    format: Int, width: Int,
                                    height: Int) {
            Log.d(TAG, "surfaceChanged()")
        }
    }

    private lateinit var manager: CameraManager
    private lateinit var cameraId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = getSystemService(CameraManager::class.java)
        setContentView(R.layout.activity_main)
        camera = null
    }

    override fun onPause() {
        super.onPause()
        surfaceview.visibility = View.GONE
        activeSession?.let {
            it.close()
            activeSession = null
        }
        camera?.let {
            it.close()
            camera = null
        }
        surfaceview.holder.removeCallback(surfaceHolderCallback)
        Log.d(TAG, "onPause()")
    }

    override fun onResume() {
        super.onResume()
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    requestCamera)
        } else {
            configureHolder()
        }
        Log.d(TAG, "onResume()")
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == requestCamera &&
                grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
            configureHolder()
        }
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
                        CameraCharacteristics.LENS_FACING_BACK) {
                    cameraId = id
                    val configs = cc.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    if (configs != null) {
                        return configs.getOutputSizes(SurfaceHolder::class.java)
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
                    || size.height > metrics.heightPixels) {
                continue
            }
            surfaceview.holder.setFixedSize(size.width, size.height)
            surfaceview.visibility = View.VISIBLE
            return
        }
        Log.d(TAG, "Zu groß")
        finish()
    }

    @Throws(SecurityException::class)
    private fun openCamera() {
        manager.openCamera(cameraId,
                object : CameraDevice.StateCallback() {
                    override fun onOpened(_camera: CameraDevice) {
                        Log.d(TAG, "onOpened()")
                        camera = _camera
                        createPreviewCaptureSession()
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        Log.d(TAG, "onDisconnected()")
                    }

                    override fun onError(camera: CameraDevice,
                                         error: Int) {
                        Log.d(TAG, "onError()")
                    }
                }, null)
    }

    private fun createPreviewCaptureSession() {
        surfaceview.holder.surface?.let {
            val outputs = arrayListOf(OutputConfiguration(it))
            val sessionConfiguration = SessionConfiguration(SessionConfiguration.SESSION_REGULAR,
                    outputs, mainExecutor, captureSessionCallback)
            try {
                builderPreview = camera?.createCaptureRequest(
                        CameraDevice.TEMPLATE_PREVIEW)
                builderPreview?.addTarget(it)
                camera?.createCaptureSession(sessionConfiguration)
            } catch (e: Exception) {
                Log.e(TAG, "createPreviewCaptureSession()", e)
            }
        }
    }
}