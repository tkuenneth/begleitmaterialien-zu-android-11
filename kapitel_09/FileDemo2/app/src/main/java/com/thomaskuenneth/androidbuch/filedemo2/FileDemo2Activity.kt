package com.thomaskuenneth.androidbuch.filedemo2

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class FileDemo2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.text = ""
        // 10 Dateien mit unterschiedlicher Länge anlegen
        for (i in 1..10) {
            val name = "Datei_$i"
            try {
                openFileOutput(name, Context.MODE_PRIVATE).use { fos ->
                    // ein Feld der Länge i mit dem Wert i füllen
                    val bytes = ByteArray(i)
                    for (j in bytes.indices) {
                        bytes[j] = i.toByte()
                    }
                    fos.write(bytes)
                }
            } catch (t: IOException) {
                tv.append("$name: ${t.message}")
            }
        }
        // Dateien ermitteln
        val files = fileList()
        // Verzeichnis ermitteln
        for (name in files) {
            val f = File(filesDir, name)
            // Länge in Bytes ermitteln
            tv.append("Länge von $name in Byte: ${f.length()}\n")
            // Datei löschen
            tv.append("Löschen ${if (!f.delete())
                "nicht " else ""}erfolgreich\n")
        }
    }
}