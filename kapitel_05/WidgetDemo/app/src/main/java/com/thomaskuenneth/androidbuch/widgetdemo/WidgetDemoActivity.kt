package com.thomaskuenneth.androidbuch.widgetdemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

private val TAG = WidgetDemoActivity::class.simpleName

class WidgetDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.widgetdemo)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val f = findViewById<FrameLayout>(R.id.frame)
        val e = findViewById<EditText>(R.id.textfield)
        val b = findViewById<Button>(R.id.apply)
        b.setOnClickListener {
            val name = e.text.toString()
            try {
                val c = Class.forName(name)
                val o = c.getDeclaredConstructor(Context::class.java)
                    .newInstance(this)
                if (o is View) {
                    f.removeAllViews()
                    f.addView(o, params)
                    f.forceLayout()
                }
            } catch (tr: Throwable) {
                val str = getString(R.string.error, name)
                Toast.makeText(this, str, Toast.LENGTH_LONG).show()
                Log.e(TAG, "Fehler beim Instanzieren von $name", tr)
            }
        }
        e.setOnEditorActionListener { _, _, _ ->
            b.performClick()
            true
        }
    }
}