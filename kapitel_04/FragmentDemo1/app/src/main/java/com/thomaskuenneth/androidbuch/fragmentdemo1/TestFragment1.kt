package com.thomaskuenneth.androidbuch.fragmentdemo1

import android.widget.TextView
import androidx.fragment.app.Fragment

class TestFragment1 : Fragment(R.layout.fragment_layout) {

    override fun onStart() {
        super.onStart()
        val textview = view as TextView
        textview.text = getString(R.string.text1)
    }
}