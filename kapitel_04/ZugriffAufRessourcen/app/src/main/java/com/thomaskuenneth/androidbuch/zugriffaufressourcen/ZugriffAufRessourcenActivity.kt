package com.thomaskuenneth.androidbuch.zugriffaufressourcen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ZugriffAufRessourcenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<TextView>(R.id.textview)
        view.append("getString(R.string.app_name): "
                + getString(R.string.app_name))
        val cal: Calendar = Calendar.getInstance()
        view.append("\n\n" + getString(R.string.datum,
                "Heute ist der",
                cal.get(Calendar.DAY_OF_YEAR),
                cal.get(Calendar.YEAR)))
        val b1 = resources.getBoolean(R.bool.bool1)
        val b2 = resources.getBoolean(R.bool.bool2)
        view.append("\n\nb1=$b1, b2=$b2")
        view.append("\n\nadams=${resources.getInteger(R.integer.adams)}")
        view.setTextColor(resources.getColor(R.color.eine_farbe, theme))
    }
}