package com.thomaskuenneth.androidbuch.permissiondemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

private const val REQUEST_READ_PHONE_NUMBER = 123
private val TAG = PermissionDemoActivity::class.simpleName
class PermissionDemoActivity : AppCompatActivity() {

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
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions,
            grantResults)
        if (requestCode == REQUEST_READ_PHONE_NUMBER) {
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
            REQUEST_READ_PHONE_NUMBER)
    }

    private fun outputLine1Number() {
        tv.text = getString(R.string.template,
            getLine1Number())
    }

    private fun getLine1Number(): String {
        var result = "???"
        getSystemService(TelephonyManager::class.java)?.run {
            try {
                result = line1Number
            } catch (ex: SecurityException) {
                Log.e(TAG, "getLine1Number()", ex)
            }
        }
        return result
    }
}