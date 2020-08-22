package com.thomaskuenneth.androidbuch.navigationdemo1

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class ChildFragment : Fragment(R.layout.fragment_child) {

    val args: ChildFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setBackgroundColor(args.color)
    }
}