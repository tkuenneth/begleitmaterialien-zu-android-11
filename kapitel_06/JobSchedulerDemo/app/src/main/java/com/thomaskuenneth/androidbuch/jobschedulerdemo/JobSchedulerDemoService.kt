package com.thomaskuenneth.androidbuch.jobschedulerdemo

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import android.widget.Toast

private val TAG = JobSchedulerDemoService::class.simpleName

class JobSchedulerDemoService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob()")
        Toast.makeText(this, R.string.job_started, Toast.LENGTH_LONG)
            .show()
        Thread {
            Log.d(TAG, "Job in Aktion")
            jobFinished(params, false)
        }.start()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob()")
        return false
    }
}