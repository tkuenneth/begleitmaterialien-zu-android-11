package com.thomaskuenneth.androidbuch.telephonymanagerdemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_PERMISSIONS = 123
class TelephonyManagerDemoActivity : AppCompatActivity() {

    private lateinit var manager: TelephonyManager

    private val listener = object : PhoneStateListener() {
        override fun onCallStateChanged(
            state: Int, incomingNumber: String
        ) {
            textview.append(
                getString(
                    R.string.template1,
                    state, incomingNumber
                )
            )
        }

        override fun onMessageWaitingIndicatorChanged(mwi: Boolean) {
            textview.append(
                getString(R.string.template2, mwi)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textview.text = ""
        manager = getSystemService(TelephonyManager::class.java)
    }

    override fun onStart() {
        super.onStart()
        if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) ||
            (checkSelfPermission(Manifest.permission.READ_CALL_LOG)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG
                ),
                REQUEST_PERMISSIONS
            )
        } else {
            listen()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS &&
            grantResults.size == 2 && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED && grantResults[1] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            listen()
        } else {
            textview.append(getString(R.string.no_permissions))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.listen(listener, PhoneStateListener.LISTEN_NONE)
    }

    private fun listen() {
        manager.listen(
            listener, PhoneStateListener.LISTEN_CALL_STATE or
                    PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR
        )
    }
}