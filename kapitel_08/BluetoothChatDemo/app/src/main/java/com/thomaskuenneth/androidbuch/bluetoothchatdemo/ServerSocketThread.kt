package com.thomaskuenneth.androidbuch.bluetoothchatdemo

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

private val TAG = ServerSocketThread::class.java.simpleName

class ServerSocketThread(
    adapter: BluetoothAdapter,
    name: String?,
    uuid: UUID
) : SocketThread() {

    private var serverSocket: BluetoothServerSocket? = null
    private var socket: BluetoothSocket? = null

    init {
        setName(TAG)
        try {
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(
                name,
                uuid
            )
        } catch (e: IOException) {
            Log.e(
                TAG,
                "listenUsingRfcommWithServiceRecord() failed",
                e
            )
        }
    }

    override fun run() {
        var keepRunning = true
        while (keepRunning) {
            try {
                socket = serverSocket?.accept()
                if (socket != null) {
                    closeServerSocket()
                    keepRunning = false
                }
            } catch (e: IOException) {
                Log.e(TAG, "accept() failed", e)
                keepRunning = false
            }
        }
    }

    override fun getSocket(): BluetoothSocket? {
        return socket
    }

    override fun cancel() {
        closeServerSocket()
        socket?.use {
            socket?.close()
        }
        socket = null
    }

    private fun closeServerSocket() {
        serverSocket?.use {
            serverSocket?.close()
        }
        serverSocket = null
    }
}