package com.thomaskuenneth.androidbuch.filedemo1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private val TAG = FileDemo1Activity::class.simpleName
private val FILENAME = "$TAG.txt"
class FileDemo1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clear.setOnClickListener { edit.setText("") }
        load.setOnClickListener { load() }
        save.setOnClickListener { save(edit.text.toString()) }
        Log.d(TAG, "filesDir: ${filesDir.absolutePath}")
        load()
    }

    private fun load() {
        val sb = StringBuilder()
        try {
            openFileInput(FILENAME).use { fis ->
                InputStreamReader(fis).use { isr ->
                    BufferedReader(isr).use { br ->
                        while (true) {
                            val line = br.readLine() ?: break
                            if (sb.isNotEmpty()) {
                                sb.append('\n')
                            }
                            sb.append(line)
                        }
                    }
                }
            }
        } catch (ex: IOException) {
            Log.e(TAG, "load()", ex)
        }
        edit.setText(sb.toString())
    }

    private fun save(s: String) {
        try {
            openFileOutput(FILENAME,
                Context.MODE_PRIVATE).use { fos ->
                OutputStreamWriter(fos).use { osw ->
                    osw.write(s)
                }
            }
        } catch (ex: IOException) {
            Log.e(TAG, "save()", ex)
        }
    }
}