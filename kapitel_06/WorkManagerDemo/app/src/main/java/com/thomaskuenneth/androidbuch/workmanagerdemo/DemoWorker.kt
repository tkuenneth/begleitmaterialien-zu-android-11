package com.thomaskuenneth.androidbuch.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

const val KeyNumber = "key"
private val TAG = DemoWorker::class.simpleName

class DemoWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        Log.d(TAG, applicationContext.getString(R.string.worker_started))
        val number = inputData.getInt(KeyNumber, 42)
        while (!isStopped) {
            val rnd = (Math.random() * (number + 1)).toInt()
            if (rnd == number) {
                break
            }
            Log.d(TAG, "Math.random(): $rnd")
        }
        return Result.success()
    }
}