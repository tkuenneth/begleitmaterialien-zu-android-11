package com.thomaskuenneth.androidbuch.bluetoothscannerdemo

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private enum class BluetoothState {
    NotAvailable, Disabled, Enabled
}

class BluetoothScannerDemoActivity : AppCompatActivity() {
    private val requestEnableBluetooth = 123
    private val requestFineLocation = 321
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (BluetoothDevice.ACTION_FOUND == intent.action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(
                    BluetoothDevice.EXTRA_DEVICE)
                tv.append(getString(R.string.template,
                    device?.name,
                    device?.address))
            }
        }
    }

    private var started = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onStart() {
        super.onStart()
        started = false
        if (getBluetoothState() == BluetoothState.NotAvailable) {
            tv.text = getString(R.string.not_available)
        } else {
            if (checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    requestFineLocation)
            } else {
                showDevices()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (started) {
            adapter?.cancelDiscovery()
            started = false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String?>,
                                            grantResults: IntArray) {
        if (requestCode == requestFineLocation &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showDevices()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            requestCode == requestEnableBluetooth) {
            showDevices()
        }
    }

    private fun getBluetoothState(): BluetoothState {
        val state = if (adapter != null) {
            if (adapter.isEnabled) {
                BluetoothState.Enabled
            } else {
                BluetoothState.Disabled
            }
        } else {
            BluetoothState.NotAvailable
        }
        if (state == BluetoothState.Disabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, requestEnableBluetooth)
        }
        return state
    }

    private fun showDevices() {
        val sb = StringBuilder()
        sb.append(getString(R.string.paired))
        adapter?.bondedDevices?.forEach {
            sb.append(getString(R.string.template,
                it.name,
                it.address))
        }
        sb.append("\n")
        if (started) {
            adapter?.cancelDiscovery()
        }
        started = adapter?.startDiscovery() ?: false
        if (started) {
            sb.append(getString(R.string.others))
        }
        tv.text = sb.toString()
    }
}