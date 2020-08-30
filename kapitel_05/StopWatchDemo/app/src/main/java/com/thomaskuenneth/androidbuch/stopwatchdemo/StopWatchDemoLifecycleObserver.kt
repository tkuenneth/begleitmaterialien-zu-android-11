package com.thomaskuenneth.androidbuch.stopwatchdemo

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

class StopwatchLifecycleObserver internal constructor(private val model: StopwatchViewModel) :
    LifecycleObserver {

    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startTimer() {
        timer = Timer()
        val running = model.running.value ?: false
        if (running) {
            scheduleAtFixedRate()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopTimer() {
        timer.cancel()
    }

    fun stop() {
        timerTask.cancel()
    }

    fun scheduleAtFixedRate() {
        val now = System.currentTimeMillis()
        val diff = model.diff.value ?: now
        model.started.value = now - diff
        timerTask = object : TimerTask() {
            override fun run() {
                model.started.value?.let {
                    model.diff.postValue(System.currentTimeMillis() - it)
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 200)
    }
}