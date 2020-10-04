package com.thomaskuenneth.androidbuch.halloandroid

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged

class MainActivity : AppCompatActivity() {

    private lateinit var message: TextView
    private lateinit var nextFinish: Button
    private lateinit var input: EditText

    private var firstClick = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        message = findViewById(R.id.message)
        nextFinish = findViewById(R.id.next_finish)
        input = findViewById(R.id.input)
        input.setOnEditorActionListener(fun(_, _, _): Boolean {
            if (nextFinish.isEnabled) {
                nextFinish.performClick()
            }
            return true
        })

        message.setText(R.string.welcome)
        nextFinish.setText(R.string.next)
        nextFinish.setOnClickListener(fun(_: View) {
            if (firstClick) {
                message.text = getString(
                    R.string.hello,
                    input.text
                )
                input.visibility = View.INVISIBLE
                nextFinish.setText(R.string.finish)
                firstClick = false
            } else {
                finish()
            }
        })

        input.doAfterTextChanged {
            nextFinish.isEnabled = it?.isNotEmpty() ?: false
        }
        nextFinish.isEnabled = false
    }
}