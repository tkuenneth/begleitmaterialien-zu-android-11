package com.thomaskuenneth.androidbuch.jobschedulerdemo

import android.app.job.*
import android.util.Log
import android.widget.Toast
import kotlin.concurrent.thread

private val TAG = JobSchedulerDemoService::class.simpleName
class JobSchedulerDemoService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob()")
        Toast.makeText(this, R.string.job_started, Toast.LENGTH_LONG)
            .show()
        thread {
            Log.d(TAG, "Job in Aktion")
            jobFinished(params, false)
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob()")
        return false
    }
}