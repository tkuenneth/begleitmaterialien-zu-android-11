package com.thomaskuenneth.androidbuch.stopwatchdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopwatchViewModel : ViewModel() {
    val running = MutableLiveData<Boolean>()
    val diff = MutableLiveData<Long>()
    val started = MutableLiveData<Long>()

    init {
        running.value = false
        diff.value = 0
        started.value = 0
    }
}