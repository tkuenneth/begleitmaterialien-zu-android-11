package com.thomaskuenneth.androidbuch.filedemo3

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.*

private val TAG = FileDemo3Activity::class.simpleName
class FileDemo3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // zwei leere Dateien erzeugen
        createFile(filesDir, "A")
        createFile(filesDir, "B")
        // Verzeichnis audio erstellen
        val dirAudio = getDir("audio", Context.MODE_PRIVATE)
        // zwei leere Dateien erzeugen
        createFile(dirAudio, "C")
        createFile(dirAudio, "D")
        // Verzeichnis video erstellen
        val dirVideo = getDir("video", Context.MODE_PRIVATE)
        // zwei leere Dateien erzeugen
        createFile(dirVideo, "E")
        createFile(dirVideo, "F")
        // temporäre Datei anlegen
        try {
            Log.d(TAG, "java.io.tmpdir: ${
                System.getProperty("java.io.tmpdir")}")
            File.createTempFile("Datei_", ".txt").apply {
                Log.d(TAG, "---> $absolutePath")
            }
        } catch (e: IOException) {
            Log.e(TAG, " createTempFile()", e)
        }
        // temporäre Datei im Cache-Verzeichnis
        Log.d(TAG, "cacheDir: ${cacheDir.absolutePath}")
        try {
            File.createTempFile("Datei_", ".txt", cacheDir).apply {
                Log.d(TAG, "---> $absolutePath")
            }
        } catch (e: IOException) {
            Log.e(TAG, " createTempFile()", e)
        }
    }

    private fun createFile(dir: File, name: String) {
        val file = File(dir, name)
        try {
            FileOutputStream(file).use { fos ->
                Log.d(TAG, file.absolutePath)
                fos.write("Hallo".toByteArray())
            }
        } catch (e: IOException) {
            Log.e(TAG, "createFile()", e)
        }
    }
}