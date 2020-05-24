package com.thomaskuenneth.androidbuch.multiwindowdemo

import android.app.Activity
import android.os.Bundle

class ChildActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.child)
    }
}