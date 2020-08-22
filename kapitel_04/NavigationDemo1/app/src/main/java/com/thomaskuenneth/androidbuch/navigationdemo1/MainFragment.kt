package com.thomaskuenneth.androidbuch.navigationdemo1

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.b1)?.setOnClickListener {
            val action =
                MainFragmentDirections.mainToChildFragment(Color.GREEN)
            findNavController().navigate(action)
        }
        view.findViewById<Button>(R.id.b2)?.setOnClickListener {
            val action =
                MainFragmentDirections.mainToChildFragment(Color.RED)
            findNavController().navigate(action)
        }
    }
}