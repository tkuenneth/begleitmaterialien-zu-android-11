package com.thomaskuenneth.androidbuch.externalstoragedemo

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

private val TAG = ExternalStorageDemoActivity::class.simpleName

class ExternalStorageDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.text = ""
        showExternalStorageState()
        // Grafik erzeugen und speichern
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(dir, "grafik.png")
        try {
            FileOutputStream(file).use { fos -> saveBitmap(fos) }
            tv.append("\n${file.absolutePath}")
        } catch (e: IOException) {
            Log.e(TAG, "FileOutputStream()", e)
        }
    }

    private fun showExternalStorageState() {
        tv.append(
            getString(
                if (Environment.isExternalStorageRemovable())
                    R.string.removable else R.string.not_removable
            )
        )
        // Status abfragen
        val state: String = Environment.getExternalStorageState()
        val canRead: Boolean
        val canWrite: Boolean
        when (state) {
            Environment.MEDIA_MOUNTED -> {
                canRead = true
                canWrite = true
            }
            Environment.MEDIA_MOUNTED_READ_ONLY -> {
                canRead = true
                canWrite = false
            }
            else -> {
                canRead = false
                canWrite = false
            }
        }
        tv.append(
            getString(
                if (canRead) R.string.can_read
                else R.string.cannot_read
            )
        )
        tv.append(
            getString(
                if (canWrite) R.string.can_write
                else R.string.cannot_write
            )
        )
        tv.append("Wird emuliert: ${Environment.isExternalStorageEmulated()}\n")
    }

    private fun saveBitmap(stream: OutputStream) {
        val w = 100
        val h = 100
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        val c = Canvas(bm)
        val paint = Paint()
        paint.textAlign = Align.CENTER
        paint.color = Color.WHITE
        c.drawRect(0f, 0f, w - 1f, h - 1f, paint)
        paint.color = Color.BLUE
        c.drawLine(0f, 0f, w - 1f, h - 1f, paint)
        c.drawLine(0f, h - 1f, w - 1f, 0f, paint)
        paint.color = Color.BLACK
        c.drawText("Hallo Android!", w / 2f, h / 2f, paint)
        bm.compress(CompressFormat.PNG, 100, stream)
    }
}