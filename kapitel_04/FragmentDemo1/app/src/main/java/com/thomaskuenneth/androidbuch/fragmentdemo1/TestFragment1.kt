package com.thomaskuenneth.androidbuch.fragmentdemo1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TestFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_layout,
            container, false
        )
    }

    override fun onStart() {
        super.onStart()
        val textview = view as TextView
        textview.text = getString(R.string.text1)
    }
}