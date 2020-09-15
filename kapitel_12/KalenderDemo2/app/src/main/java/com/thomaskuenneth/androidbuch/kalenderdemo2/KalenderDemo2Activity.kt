package com.thomaskuenneth.androidbuch.kalenderdemo2

import android.Manifest.permission
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.provider.CalendarContract.Events
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_READ_CALENDAR = 123
class KalenderDemo2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(permission.READ_CALENDAR)
                != PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission.READ_CALENDAR),
                    REQUEST_READ_CALENDAR)
        } else {
            logEvents()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CALENDAR &&
                grantResults.isNotEmpty() &&
                grantResults[0] == PERMISSION_GRANTED) {
            logEvents()
        } else {
            textview.text = getString(R.string.no_permission)
        }
    }

    private fun logEvents() {
        contentResolver.query(Events.CONTENT_URI, null, null,
                null, null)?.run {
            val indexId = getColumnIndex(Events._ID)
            val indexTitle = getColumnIndex(Events.TITLE)
            while (moveToNext()) {
                textview.append("_ID: ${getString(indexId)}\n")
                textview.append("TITLE: ${getString(indexTitle)}\n\n")
            }
            close()
        }
    }
}