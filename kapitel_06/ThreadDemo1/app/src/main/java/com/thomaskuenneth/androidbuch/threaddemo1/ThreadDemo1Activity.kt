package com.thomaskuenneth.androidbuch.threaddemo1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

private val TAG = ThreadDemo1Activity::class.simpleName

class ThreadDemo1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val r = Runnable { Log.d(TAG, "run()-Methode wurde aufgerufen") }
        val t = Thread(r)
        t.start()
        Log.d(TAG, "Thread wurde gestartet")
        val fib = Thread(fibRunner(20))
        fib.start()
        Log.d(TAG, "t.isAlive(): ${t.isAlive}")
        Thread(bewegeGegner1()).start()
    }

    private fun fibRunner(num: Int): Runnable {
        return object : Runnable {
            override fun run() {
                val result = fib(num)
                Log.d(TAG, "fib($num) = $result")
            }

            private fun fib(n: Int): Int {
                return when (n) {
                    0 -> 0
                    1 -> 1
                    else -> {
                        Thread.yield()
                        fib(n - 1) + fib(n - 2)
                    }
                }
            }
        }
    }

    private fun bewegeGegner1(): Runnable {
        return Runnable {
            while (true) {
                Log.i(TAG, "bewege Gegner 1")
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "sleepTester()", e)
                }
            }
        }
    }

    @Volatile
    private var keepRunning = false

    override fun onStart() {
        super.onStart()
        // Thread erzeugen
        val t = Thread(bewegeGegner2())
        keepRunning = true
        // Thread starten
        t.start()
    }

    override fun onPause() {
        super.onPause()
        keepRunning = false
    }

    private fun bewegeGegner2() = Runnable {
        while (keepRunning) {
            Log.i(TAG, "bewege Gegner 2")
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                Log.e(TAG, "sleepTester()", e)
            }
        }
    }
}