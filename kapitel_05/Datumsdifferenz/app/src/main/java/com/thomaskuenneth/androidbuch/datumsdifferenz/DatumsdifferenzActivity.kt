package com.thomaskuenneth.androidbuch.datumsdifferenz

import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DatumsdifferenzActivity : AppCompatActivity() {

    private var cal1 = Calendar.getInstance()
    private var cal2 = Calendar.getInstance()

    private lateinit var tv: TextView
    private lateinit var dp1: DatePicker
    private lateinit var dp2: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.datumsdifferenz)
        dp1 = findViewById(R.id.date1)
        dp1.init(cal1.get(Calendar.YEAR),
                cal1.get(Calendar.MONTH),
                cal1.get(Calendar.DAY_OF_MONTH),
                null)
        dp2 = findViewById(R.id.date2)
        dp2.init(cal2.get(Calendar.YEAR),
                cal2.get(Calendar.MONTH),
                cal2.get(Calendar.DAY_OF_MONTH),
                null)
        tv = findViewById(R.id.textview_result)
        val b = findViewById<Button>(R.id.button_calc)
        b.setOnClickListener { berechnen() }
        berechnen()
    }

    private fun berechnen() {
        updateCalendarFromDatePicker(cal1, dp1)
        updateCalendarFromDatePicker(cal2, dp2)
        if (cal2.before(cal1)) {
            val temp = cal1
            cal1 = cal2
            cal2 = temp
        }
        var days = 0
        while (cal1[Calendar.YEAR] != cal2[Calendar.YEAR]
                || cal1[Calendar.MONTH] != cal2[Calendar.MONTH]
                || cal1[Calendar.DAY_OF_MONTH] != cal2[Calendar.DAY_OF_MONTH]) {
            days += 1
            cal1.add(Calendar.DAY_OF_YEAR, 1)
        }
        tv.text = getString(R.string.template, days)
    }

    private fun updateCalendarFromDatePicker(cal: Calendar,
                                             dp: DatePicker) {
        cal[Calendar.YEAR] = dp.year
        cal[Calendar.MONTH] = dp.month
        cal[Calendar.DAY_OF_MONTH] = dp.dayOfMonth
    }
}