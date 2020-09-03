package com.thomaskuenneth.androidbuch.bluetoothchatdemo

import android.bluetooth.BluetoothSocket

abstract class SocketThread : Thread() {
    abstract fun getSocket(): BluetoothSocket?
    abstract fun cancel()
}