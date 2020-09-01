package com.thomaskuenneth.androidbuch.jobschedulerdemo

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class JobSchedulerDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSystemService(JobScheduler::class.java)?.run {
            // ausstehende Jobs anzeigen
            val jobs = allPendingJobs
            val sb = StringBuilder()
            for (info in jobs) {
                sb.append(info.toString() + "\n")
            }
            if (sb.isEmpty()) {
                sb.append(getString(R.string.no_jobs))
            }
            val jobId = 123
            // die Klasse des Jobs
            val service = ComponentName(this@JobSchedulerDemoActivity,
                JobSchedulerDemoService::class.java)
            val jobInfo = JobInfo.Builder(jobId, service)
                // alle 20 Minuten wiederholen
                .setPeriodic(20 * 60 * 10000)
                // nur wenn das Ladekabel angeschlossen ist
                .setRequiresCharging(true)
                .build()
            // die Ausführung planen
            schedule(jobInfo)
            textview.text = sb.toString()
        }
    }
}