package com.thomaskuenneth.androidbuch.fragmentdemo3

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment

const val INDEX = "index"
class DetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        container?.let {
            val text = TextView(context)
            text.text = getString(R.string.template, 1 + getIndex())
            val scroller = ScrollView(context)
            scroller.addView(text)
            return scroller
        }
        return null
    }

    fun getIndex(): Int {
        return arguments?.getInt(INDEX, 0) ?: -1
    }
}