package com.thomaskuenneth.androidbuch.fibonaccidemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

val TAG = FibonacciDemoActivity::class.simpleName
class FibonacciDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "fib(5) = " + fib(5))
    }

    private fun fib(n: Int): Int {
        Log.i(TAG, "n=$n")
        return when (n) {
            0 -> 1
            1 -> 1
            else -> fib(n - 1) + fib(n - 2)
        }
    }
}