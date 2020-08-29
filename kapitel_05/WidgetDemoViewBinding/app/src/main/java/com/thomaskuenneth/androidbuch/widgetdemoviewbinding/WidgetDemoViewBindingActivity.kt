package com.thomaskuenneth.androidbuch.widgetdemoviewbinding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thomaskuenneth.androidbuch.widgetdemoviewbinding.databinding.WidgetdemoBinding

private val TAG = WidgetDemoViewBindingActivity::class.simpleName
class WidgetDemoViewBindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = WidgetdemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.apply.setOnClickListener {
            val name = binding.textfield.text.toString()
            try {
                val c = Class.forName(name)
                val o = c.getDeclaredConstructor(Context::class.java)
                        .newInstance(this)
                if (o is View) {
                    binding.frame.removeAllViews()
                    binding.frame.addView(o, params)
                    binding.frame.forceLayout()
                }
            } catch (tr: Throwable) {
                val str = getString(R.string.error, name)
                Toast.makeText(this, str, Toast.LENGTH_LONG).show()
                Log.e(TAG, "Fehler beim Instanzieren von $name", tr)
            }
        }
        binding.textfield.setOnEditorActionListener { _, _, _ ->
            binding.apply.performClick()
            true
        }
    }
}