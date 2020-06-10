package com.thomaskuenneth.androidbuch.bluetoothchatdemo

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

private val TAG = ServerSocketThread::class.simpleName

class ServerSocketThread(
    adapter: BluetoothAdapter,
    serviceName: String?,
    uuid: UUID
) : SocketThread() {

    private var serverSocket: BluetoothServerSocket? = null
    private var socket: BluetoothSocket? = null

    init {
        name = TAG!!
        try {
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(
                serviceName,
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
                serverSocket?.accept().run {
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