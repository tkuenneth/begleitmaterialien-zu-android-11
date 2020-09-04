package com.thomaskuenneth.androidbuch.bluetoothchatdemo

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.util.*
import kotlin.concurrent.thread

private const val REQUEST_FINE_LOCATION = 321
private val TAG = BluetoothChatDemoActivity::class.simpleName
class BluetoothChatDemoActivity : AppCompatActivity() {
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private val device1 = "..."
    private val device2 = "..."
    private val myUuid: UUID =
        UUID.fromString("dc4f9aa6-ce43-4709-bd2e-7845a3e705f1")

    private var serverThread: Thread? = null
    private var clientThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        input.isEnabled = false
    }

    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE_LOCATION
            )
        } else {
            startOrFinish()
        }
    }

    override fun onPause() {
        super.onPause()
        serverThread?.interrupt()
        serverThread = null
        clientThread?.interrupt()
        clientThread = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_FINE_LOCATION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startOrFinish()
        }
    }

    private fun startOrFinish() {
        if (isBluetoothEnabled()) {
            connect()
        } else {
            finish()
        }
    }

    private fun isBluetoothEnabled(): Boolean {
        val enabled = adapter?.isEnabled ?: false
        if (!enabled) {
            Toast.makeText(this, R.string.enable_bluetooth,
                Toast.LENGTH_LONG).show()
        }
        return enabled
    }

    private fun connect() {
        val myName = adapter?.name
        val otherName = if (device1 == myName) device2 else device1
        for (device in adapter?.bondedDevices ?:
        emptyList<BluetoothDevice>()) {
            if (otherName == device.name) {
                val serverSocketThread = ServerSocketThread(adapter,
                    TAG, myUuid)
                serverThread = createAndStartThread(serverSocketThread)
                val clientSocketThread = ClientSocketThread(device, myUuid)
                clientThread = createAndStartThread(clientSocketThread)
                input.isEnabled = true
                break
            }
        }
    }

    private fun createAndStartThread(t: SocketThread): Thread
            = thread {
        var keepRunning = true
        try {
            t.start()
            Log.d(TAG, "joining " + t.name)
            t.join()
            t.getSocket()?.run {
                Log.d(TAG, String.format("connection type %d for %s",
                    connectionType, t.name)
                )
                input.setOnEditorActionListener { _: TextView?, _: Int,
                                                  _: KeyEvent? ->
                    send(outputStream, input.text.toString())
                    runOnUiThread { input.setText("") }
                    true
                }
                while (keepRunning) {
                    receive(inputStream)?.let {
                        runOnUiThread { output?.append(it) }
                    }
                }
            }
        } catch (thr: Throwable) { // InterruptedException, IOException
            Log.e(TAG, thr.message, thr)
            keepRunning = false
        } finally {
            Log.d(TAG, "calling cancel() of " + t.name)
            t.cancel()
        }
    }

    private fun send(stream: OutputStream, text: String) {
        stream.use {
            stream.write(text.toByteArray())
        }
    }

    private fun receive(stream: InputStream): String? {
        stream.use {
            val num = stream.available()
            if (num > 0) {
                val buffer = ByteArray(num)
                var bytesToRead = num
                while (bytesToRead > 0) {
                    val read = stream.read(buffer, num - bytesToRead, bytesToRead)
                    if (read == -1) {
                        break
                    }
                    bytesToRead -= read
                }
                return String(buffer)
            }
        }
        return null
    }
}