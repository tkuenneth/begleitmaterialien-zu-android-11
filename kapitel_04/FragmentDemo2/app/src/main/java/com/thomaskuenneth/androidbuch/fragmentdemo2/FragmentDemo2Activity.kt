package com.thomaskuenneth.androidbuch.fragmentdemo2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentDemo2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val fragmentManager: FragmentManager = supportFragmentManager
            val ft: FragmentTransaction = fragmentManager.beginTransaction()
            for (i in 0..2) {
                val fragment = EinfachesFragment()
                ft.add(R.id.ll, fragment)
            }
            ft.addToBackStack(null)
            ft.commit()
        }
    }
}

class EinfachesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.einfaches_fragment,
                container, false)
    }
}