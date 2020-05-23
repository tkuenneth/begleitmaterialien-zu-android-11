package com.thomaskuenneth.androidbuch.workmanagerdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import java.time.Duration

class WorkManagerDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun handleButtonClicked(view: View) {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
        val repeatInterval = Duration.ofMillis(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS)
        val work = PeriodicWorkRequestBuilder<DemoWorker>(repeatInterval)
            .setConstraints(constraints)
            .setInputData(Data.Builder().putInt(KeyNumber, 123).build())
            .build()
        WorkManager.getInstance(this).enqueue(work)
    }
}