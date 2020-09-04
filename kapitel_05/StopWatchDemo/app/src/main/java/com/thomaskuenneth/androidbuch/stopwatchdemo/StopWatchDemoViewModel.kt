package com.thomaskuenneth.androidbuch.stopwatchdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopWatchDemoViewModel : ViewModel() {
    val running = MutableLiveData(false)
    val diff = MutableLiveData(0L)
    val started = MutableLiveData(0L)
}