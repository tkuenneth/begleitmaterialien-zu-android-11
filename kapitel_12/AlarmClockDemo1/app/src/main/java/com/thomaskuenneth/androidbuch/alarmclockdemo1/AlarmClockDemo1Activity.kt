package com.thomaskuenneth.androidbuch.alarmclockdemo1

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class AlarmClockDemo1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        group.check(R.id.alarm)
        go.setOnClickListener {
            when (group.checkedRadioButtonId) {
                R.id.alarm -> fireAlarm()
                R.id.timer -> fireTimer()
            }
        }
    }

    private fun fireAlarm() {
        val alarm = Intent(AlarmClock.ACTION_SET_ALARM)
        alarm.putExtra(AlarmClock.EXTRA_MESSAGE, "Ein Alarm")
        alarm.putExtra(AlarmClock.EXTRA_HOUR, 20)
        alarm.putExtra(AlarmClock.EXTRA_MINUTES, 0)
        alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, false)
        startActivity(alarm)
    }

    private fun fireTimer() {
        val timer = Intent(AlarmClock.ACTION_SET_TIMER)
        timer.putExtra(AlarmClock.EXTRA_MESSAGE, "Ein Timer")
        timer.putExtra(AlarmClock.EXTRA_LENGTH, 90)
        timer.putExtra(AlarmClock.EXTRA_SKIP_UI, false)
        startActivity(timer)
    }
}