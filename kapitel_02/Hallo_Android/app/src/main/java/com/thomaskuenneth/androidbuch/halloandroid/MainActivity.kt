package com.thomaskuenneth.androidbuch.halloandroid

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged

class MainActivity : AppCompatActivity() {

    private lateinit var nachricht: TextView
    private lateinit var weiterFertig: Button
    private lateinit var eingabe: EditText

    private var ersterKlick = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nachricht = findViewById(R.id.nachricht)
        weiterFertig = findViewById(R.id.weiter_fertig)
        eingabe = findViewById(R.id.eingabe)
        eingabe.setOnEditorActionListener(fun(_, _, _): Boolean {
            if (weiterFertig.isEnabled) {
                weiterFertig.performClick()
            }
            return true
        })

        nachricht.setText(R.string.willkommen)
        weiterFertig.setText(R.string.weiter)
        weiterFertig.setOnClickListener(fun(_: View) {
            if (ersterKlick) {
                nachricht.text = getString(
                    R.string.hallo,
                    eingabe.text
                )
                eingabe.visibility = View.INVISIBLE
                weiterFertig.setText(R.string.fertig)
                ersterKlick = false
            } else {
                finish()
            }
        })

        eingabe.doAfterTextChanged {
            weiterFertig.isEnabled = it?.isNotEmpty() ?: false
        }
        weiterFertig.isEnabled = false
    }
}