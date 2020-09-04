package com.thomaskuenneth.androidbuch.blescannerdemo

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_FINE_LOCATION = 321
private val TAG = BLEScannerDemoActivity::class.simpleName
class BLEScannerDemoActivity : AppCompatActivity() {

    private val scanCallback = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            Toast.makeText(this@BLEScannerDemoActivity,
                getString(R.string.error, errorCode), Toast.LENGTH_LONG).show()
        }

        override fun onScanResult(callbackType: Int,
                                  result: ScanResult?) {
            updateData(result)
        }

        override fun onBatchScanResults(results: List<ScanResult?>) {
            for (result in results) {
                updateData(result)
            }
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onServicesDiscovered(gatt: BluetoothGatt,
                                          status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                logGattServices(gatt)
            } else {
                Log.d(TAG, "onServicesDiscovered: $status")
            }
            gatt.close()
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt,
                                             status: Int, newState: Int) {
            Log.d(TAG, "Status der Verbindung: $newState")
        }
    }

    private lateinit var listAdapter: ArrayAdapter<String>
    private val scanResults = HashMap<String?, ScanResult?>()
    private var adapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1)
        lv.adapter = listAdapter
        lv.setOnItemClickListener { _, _, pos, _ ->
            val address = listAdapter.getItem(pos)
            val result = scanResults[address]
            info(result)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter = null
        listAdapter.clear()
        scanResults.clear()
        tv.text = ""
        if (checkSelfPermission(ACCESS_FINE_LOCATION) !=
            PERMISSION_GRANTED) {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_FINE_LOCATION)
        } else {
            startOrFinish()
        }
    }

    override fun onPause() {
        super.onPause()
        if (adapter != null) {
            scan(false)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String?>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_FINE_LOCATION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PERMISSION_GRANTED) {
            startOrFinish()
        } else {
            finish()
        }
    }

    private fun startOrFinish() {
        if (isBluetoothEnabled()) {
            scan(true)
        } else {
            finish()
        }
    }

    private fun isBluetoothEnabled(): Boolean {
        adapter = null
        var enabled = false
        getSystemService(BluetoothManager::class.java)?.let {
            adapter = it.adapter
            enabled = adapter?.isEnabled ?: false
        }
        if (!enabled) {
            Toast.makeText(this, R.string.enable_bluetooth, Toast.LENGTH_LONG)
                .show()
        }
        return enabled
    }

    private fun scan(enable: Boolean) {
        val scanner = adapter?.bluetoothLeScanner
        if (enable) {
            scanner?.startScan(scanCallback)
        } else {
            scanner?.stopScan(scanCallback)
        }
    }

    private fun updateData(result: ScanResult?) {
        result?.device?.address.let {
            if (!scanResults.containsKey(it)) {
                listAdapter.add(it)
                listAdapter.notifyDataSetChanged()
            }
            scanResults.put(it, result)
        }
    }

    private fun info(result: ScanResult?) {
        tv.text = result?.toString()
        val device = result?.device
        val gatt = device?.connectGatt(this,
            true, gattCallback)
        val started = gatt?.discoverServices()
        Log.d(TAG, "discoverServices(): $started")
    }

    private fun logGattServices(gatt: BluetoothGatt) {
        gatt.services.forEach {
            Log.d(TAG, "Service " + it.uuid.toString())
        }
    }
}