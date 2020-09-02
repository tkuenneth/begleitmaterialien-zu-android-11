package com.thomaskuenneth.androidbuch.subscriptionmanagerdemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SubscriptionManager
import android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private val TAG = SubscriptionManagerDemoActivity::class.simpleName
class SubscriptionManagerDemoActivity : AppCompatActivity() {

    private val requestReadPhoneState = 123
    private lateinit var manager: SubscriptionManager
    private val listener = object : OnSubscriptionsChangedListener() {
        override fun onSubscriptionsChanged() {
            Log.d(TAG, "onSubscriptionsChanged()")
            output()
        }
    }
    private var listenerWasRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            manager = getSystemService(SubscriptionManager::class.java)
        } catch (ex: RuntimeException) {
            Log.e(TAG, "getSystemService()", ex)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestReadPhoneState &&
            grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            output()
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            output()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                requestReadPhoneState
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (listenerWasRegistered) {
            manager.removeOnSubscriptionsChangedListener(listener)
            listenerWasRegistered = false
        }
    }

    private fun output() {
        if (!listenerWasRegistered) {
            manager.addOnSubscriptionsChangedListener(listener)
            listenerWasRegistered = true
        }
        layout.removeAllViews()
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        try {
            manager.activeSubscriptionInfoList?.forEach {
                Log.d(
                    TAG, "getCarrierName(): ${it.carrierName}"
                )
                Log.d(
                    TAG, "getDisplayName(): ${it.displayName}"
                )
                Log.d(
                    TAG, "getDataRoaming(): ${it.dataRoaming}"
                )
                val imageview = ImageView(this)
                imageview.layoutParams = params
                imageview.setImageBitmap(it.createIconBitmap(this))
                layout.addView(imageview)
            }
        } catch (ex: SecurityException) {
            Log.e(TAG, "activeSubscriptionInfoList", ex)
        }
        layout.invalidate()
    }
}