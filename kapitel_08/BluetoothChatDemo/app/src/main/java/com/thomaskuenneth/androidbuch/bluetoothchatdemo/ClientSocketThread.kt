package com.thomaskuenneth.androidbuch.bluetoothchatdemo

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

private val TAG = ClientSocketThread::class.simpleName
class ClientSocketThread(device: BluetoothDevice, uuid: UUID) :
    SocketThread() {

    private var socket: BluetoothSocket? = null

    init {
        name = TAG!!
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid)
        } catch (e: IOException) {
            Log.e(TAG, "createRfcommSocketToServiceRecord() failed", e)
        }
    }

    override fun run() {
        try {
            socket?.connect()
        } catch (connectException: IOException) {
            cancel()
        }
    }

    override fun getSocket(): BluetoothSocket? {
        return socket
    }

    override fun cancel() {
        socket?.use {
            socket?.close()
        }
        socket = null
    }
}