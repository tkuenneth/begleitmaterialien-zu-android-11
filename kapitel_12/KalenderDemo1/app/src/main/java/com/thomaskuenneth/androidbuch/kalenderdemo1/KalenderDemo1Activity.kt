package com.thomaskuenneth.androidbuch.kalenderdemo1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

private val TAG = KalenderDemo1Activity::class.simpleName
class KalenderDemo1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            // Beginn und Ende eines Termins
            val cal = Calendar.getInstance()
            val from = cal.time
            cal.add(Calendar.HOUR_OF_DAY, 1)
            val to = cal.time
            // Termin anlegen
            createEntry(getString(R.string.title),
                    getString(R.string.hello), from, to,
                    cal.get(Calendar.HOUR_OF_DAY) < 12)
        }
    }

    private fun createEntry(title: String, description: String,
                            from: Date, to: Date, allDay: Boolean) {
        val intent = Intent(Intent.ACTION_INSERT,
                Events.CONTENT_URI)
        intent.putExtra(Events.TITLE, title)
        intent.putExtra(Events.DESCRIPTION, description)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                from.time)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, to.time)
        intent.putExtra(Events.ALL_DAY, allDay)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "keine passende Activity", e)
        }
    }
}