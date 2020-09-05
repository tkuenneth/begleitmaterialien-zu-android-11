package com.thomaskuenneth.androidbuch.calllogdemo

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.database.Cursor
import android.os.*
import android.provider.CallLog
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

private const val REQUEST_READ_CALL_LOG = 123
private const val REQUEST_WRITE_CALL_LOG = 321
class CallLogDemoActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val contentObserver = object : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            updateAdapter()
        }
    }
    private var contentObserverWasRegistered = false
    private lateinit var cursorAdapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cursorAdapter = SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1,
            null, arrayOf(CallLog.Calls.NUMBER),
            intArrayOf(android.R.id.text1), 0)
        cursorAdapter.viewBinder = SimpleCursorAdapter.ViewBinder {
                view: View, cursor: Cursor, columnIndex: Int ->
            if (columnIndex ==
                cursor.getColumnIndex(CallLog.Calls.NUMBER)) {
                var number = cursor.getString(columnIndex)
                val isNew = cursor.getInt(cursor.getColumnIndex(
                    CallLog.Calls.NEW))
                if (isNew != 0) {
                    number += " (neu)"
                }
                (view as TextView).text = number
                true
            } else {
                false
            }
        }
        listview.adapter = cursorAdapter
        listview.onItemClickListener = OnItemClickListener {
                _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            val c = cursorAdapter.getItem(position) as Cursor
            val callLogId = c.getLong(c.getColumnIndex(
                CallLog.Calls._ID))
            updateCallLogData(callLogId)
        }
        updateAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (contentObserverWasRegistered) {
            contentResolver.unregisterContentObserver(contentObserver)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_READ_CALL_LOG -> updateAdapter()
                REQUEST_WRITE_CALL_LOG -> {
                }
            }
        }
    }

    private fun updateAdapter() {
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CALL_LOG),
                REQUEST_READ_CALL_LOG)
        } else {
            if (!contentObserverWasRegistered) {
                contentResolver.registerContentObserver(
                    CallLog.Calls.CONTENT_URI,
                    false, contentObserver)
                contentObserverWasRegistered = true
            }
            thread {
                val c = getMissedCalls()
                runOnUiThread { cursorAdapter.changeCursor(c) }
            }
        }
        if (checkSelfPermission(Manifest.permission.WRITE_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                Manifest.permission.WRITE_CALL_LOG),
                REQUEST_WRITE_CALL_LOG)
        }
    }

    @Throws(SecurityException::class)
    private fun getMissedCalls(): Cursor? {
        val projection = arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.DATE,
                CallLog.Calls.NEW, CallLog.Calls._ID)
        val selection = CallLog.Calls.TYPE + " = ?"
        val selectionArgs = arrayOf(CallLog.Calls.MISSED_TYPE.toString())
        return contentResolver.query(CallLog.Calls.CONTENT_URI,
                projection, selection,
                selectionArgs, null)
    }

    private fun updateCallLogData(id: Long) {
        if (checkSelfPermission(Manifest.permission.WRITE_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED) {
            val values = ContentValues()
            values.put(CallLog.Calls.NEW, 0)
            val where = CallLog.Calls._ID + " = ?"
            val selectionArgs = arrayOf(id.toString())
            contentResolver.update(CallLog.Calls.CONTENT_URI,
                    values, where, selectionArgs)
        }
    }
}