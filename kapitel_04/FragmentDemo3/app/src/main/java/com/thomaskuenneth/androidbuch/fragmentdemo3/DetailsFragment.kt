package com.thomaskuenneth.androidbuch.fragmentdemo3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment

const val INDEX = "index"

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var scroller: ScrollView? = null
        if (container != null) {
            scroller = ScrollView(context)
            val text = TextView(context)
            scroller.addView(text)
            text.text = getString(
                R.string.template,
                1 + getIndex()
            )
        }
        return scroller
    }

    fun getIndex(): Int {
        return arguments?.getInt(INDEX, 0) ?: -1
    }
}