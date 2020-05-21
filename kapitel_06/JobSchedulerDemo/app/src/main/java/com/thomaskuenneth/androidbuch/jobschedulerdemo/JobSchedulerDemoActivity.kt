package com.thomaskuenneth.androidbuch.jobschedulerdemo

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class JobSchedulerDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.textview)
        val scheduler = getSystemService(JobScheduler::class.java)
        if (scheduler != null) {
            // ausstehende Jobs anzeigen
            val jobs = scheduler.allPendingJobs
            val sb = StringBuilder()
            for (info in jobs) {
                sb.append(info.toString() + "\n")
            }
            if (sb.isEmpty()) {
                sb.append(getString(R.string.no_jobs))
            }
            val jobId = 123
            // die Klasse des Jobs
            val serviceEndpoint = ComponentName(this,
                    JobSchedulerDemoService::class.java)
            val jobInfo = JobInfo.Builder(jobId, serviceEndpoint)
                    // alle 20 Minuten wiederholen
                    .setPeriodic(20 * 60 * 10000)
                    // nur wenn das Ladekabel angeschlossen ist
                    .setRequiresCharging(true)
                    .build()
            // die Ausführung planen
            scheduler.schedule(jobInfo)
            tv.text = sb.toString()
        }
    }
}