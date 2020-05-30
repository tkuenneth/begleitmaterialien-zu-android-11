package com.thomaskuenneth.androidbuch.calllogdemo

import android.Manifest
import android.app.ListActivity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.database.Cursor
import android.os.*
import android.provider.CallLog
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.cursoradapter.widget.SimpleCursorAdapter

class CallLogDemoActivity : ListActivity() {

    private val requestReadCallLog = 123
    private val requestWriteCallLOg = 321

    private val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            updateAdapter()
        }
    }
    private var contentObserverWasRegistered = false
    private lateinit var cursorAdapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cursorAdapter = SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null, arrayOf(CallLog.Calls.NUMBER), intArrayOf(android.R.id.text1), 0)
        cursorAdapter.viewBinder = SimpleCursorAdapter.ViewBinder { view: View, cursor: Cursor, columnIndex: Int ->
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
        listAdapter = cursorAdapter
        listView.onItemClickListener = OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
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
                requestReadCallLog -> updateAdapter()
                requestWriteCallLOg -> {
                }
            }
        }
    }

    private fun updateAdapter() {
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                    Manifest.permission.READ_CALL_LOG),
                    requestReadCallLog)
        } else {
            if (!contentObserverWasRegistered) {
                contentResolver.registerContentObserver(
                        CallLog.Calls.CONTENT_URI,
                        false, contentObserver)
                contentObserverWasRegistered = true
            }
            val t = Thread(Runnable {
                val c = getMissedCalls()
                runOnUiThread { cursorAdapter.changeCursor(c) }
            })
            t.start()
        }
        if (checkSelfPermission(Manifest.permission.WRITE_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                    Manifest.permission.WRITE_CALL_LOG),
                    requestWriteCallLOg)
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