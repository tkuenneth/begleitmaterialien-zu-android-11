package com.thomaskuenneth.androidbuch.permissiondemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


private val TAG = PermissionDemoActivity::class.simpleName

class PermissionDemoActivity : AppCompatActivity() {

    private val rqReadPhoneNumbers = 123

    private lateinit var tv: TextView
    private lateinit var bt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.tv)
        bt = findViewById(R.id.bt)
        bt.setOnClickListener { requestPermission() }
    }

    override fun onStart() {
        super.onStart()
        bt.visibility = View.GONE
        if (checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_PHONE_NUMBERS)) {
                tv.setText(R.string.explain1)
                bt.visibility = View.VISIBLE
            } else {
                requestPermission()
            }
        } else {
            outputLine1Number()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == rqReadPhoneNumbers) {
            bt.visibility = View.GONE
            if (grantResults.isNotEmpty() && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                outputLine1Number()
            } else {
                tv.setText(R.string.explain2)
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(
                Manifest.permission.READ_PHONE_NUMBERS),
                rqReadPhoneNumbers)
    }

    private fun outputLine1Number() {
        tv.text = getString(R.string.template,
                getLine1Number())
    }

    private fun getLine1Number(): String {
        var result = "???"
        val tm = getSystemService(TelephonyManager::class.java)
        if (tm != null) {
            try {
                result = tm.line1Number
            } catch (ex: SecurityException) {
                Log.e(TAG, "getLine1Number()", ex)
            }
        }
        return result
    }
}