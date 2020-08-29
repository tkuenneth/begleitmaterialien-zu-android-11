package com.thomaskuenneth.androidbuch.widgetdemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

private val TAG = WidgetDemoActivity::class.simpleName
class WidgetDemoActivity : AppCompatActivity() {

    private lateinit var frame: FrameLayout
    private lateinit var textfield: EditText
    private lateinit var apply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.widgetdemo)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        frame = findViewById(R.id.frame)
        textfield = findViewById(R.id.textfield)
        apply = findViewById(R.id.apply)
        apply.setOnClickListener {
            val name = textfield.text.toString()
            try {
                val c = Class.forName(name)
                val o = c.getDeclaredConstructor(Context::class.java)
                    .newInstance(this)
                if (o is View) {
                    frame.removeAllViews()
                    frame.addView(o, params)
                    frame.forceLayout()
                }
            } catch (tr: Throwable) {
                val str = getString(R.string.error, name)
                Toast.makeText(this, str, Toast.LENGTH_LONG).show()
                Log.e(TAG, "Fehler beim Instanzieren von $name", tr)
            }
        }
        textfield.setOnEditorActionListener { _, _, _ ->
            apply.performClick()
            true
        }
    }
}