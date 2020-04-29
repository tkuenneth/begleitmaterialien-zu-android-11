package com.thomaskuenneth.androidbuch.fragmentdemo3

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.configuration.orientation
            == Configuration.ORIENTATION_LANDSCAPE
        ) {
            finish()
        }
        if (savedInstanceState == null) {
            val details = DetailsFragment()
            details.arguments = intent.extras
            supportFragmentManager.beginTransaction().add(android.R.id.content, details).commit()
        }
    }
}
