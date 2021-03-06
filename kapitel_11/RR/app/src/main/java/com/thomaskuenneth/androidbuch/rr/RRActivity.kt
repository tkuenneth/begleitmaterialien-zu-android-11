package com.thomaskuenneth.androidbuch.rr

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.thomaskuenneth.androidbuch.rr.Mode.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

fun getBaseDir(ctx: Context): File? {
    // für Zugriff auf dieses Verzeichnis sind
    // ab KitKat keine Berechtigungen nötig
    val dir = File(
        ctx.getExternalFilesDir(null),
        ".RR"
    )
    if (!dir.mkdirs()) {
        Log.d(TAG, "Verzeichnisse schon vorhanden")
    }
    return dir
}

private enum class Mode {
    Waiting, Recording, Playing
}

private const val REQUEST_RECORD_AUDIO = 123
private val TAG = RRActivity::class.simpleName
class RRActivity : AppCompatActivity() {
    private var mode = Waiting
    private var currentFile: File? = null
    private var player: MediaPlayer? = null
    private var recorder: MediaRecorder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listAdapter = RRListAdapter(this)
        lv.adapter = listAdapter
        lv.setOnItemClickListener { _, _, position, _ ->
            listAdapter.getItem(position)?.let {
                playAudioFile(it.absolutePath)
            }
        }
        lv.setOnItemLongClickListener { _, _, position, _ ->
            listAdapter.getItem(position)?.let {
                if (it.delete()) {
                    listAdapter.remove(it)
                }
            }
            true
        }
        b.setOnClickListener {
            when (mode) {
                Waiting -> {
                    currentFile = recordToFile()
                }
                Recording -> {
                    // die Aufnahme stoppen
                    recorder?.stop()
                    releaseRecorder()
                    listAdapter.add(currentFile)
                    currentFile = null
                    mode = Waiting
                    updateButtonText()
                }
                Playing -> {
                    player?.stop()
                    releasePlayer()
                    mode = Waiting
                    updateButtonText()
                }
            }
        }
        currentFile = null
        mode = Waiting
        player = null
        recorder = null
        updateButtonText()
    }

    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            b.isEnabled = false
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO
            )
        } else {
            b.isEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_RECORD_AUDIO &&
            grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            b.isEnabled = true
        }
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
        releaseRecorder()
    }

    private fun updateButtonText() {
        b.text = getString(if (mode != Waiting) R.string.finish
        else R.string.record)
    }

    private fun recordToFile(): File? {
        recorder = MediaRecorder()
        recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        RRFile(
            getBaseDir(this), System
                .currentTimeMillis().toString() + EXT_3GP
        ).let { f ->
            try {
                if (!f.createNewFile()) {
                    Log.d(TAG, "Datei schon vorhanden")
                }
                recorder?.setOutputFile(f.absolutePath)
                recorder?.prepare()
                recorder?.start()
                mode = Recording
                updateButtonText()
            } catch (e: IOException) {
                Log.e(TAG, "Konnte Aufnahme nicht starten", e)
            }
            return f
        }
    }

    // Recorder-Ressourcen freigeben
    private fun releaseRecorder() {
        recorder?.release()
        recorder = null
    }

    private fun playAudioFile(filename: String) {
        player = MediaPlayer()
        player?.setOnCompletionListener {
            releasePlayer()
            mode = Waiting
            updateButtonText()
        }
        try {
            player?.setDataSource(filename)
            player?.prepare()
            player?.start()
            mode = Playing
            updateButtonText()
        } catch (thr: Exception) {
            Log.e(TAG, "konnte Audio nicht wiedergeben", thr)
        }
    }

    // Player-Ressourcen freigeben
    private fun releasePlayer() {
        player?.release()
        player = null
    }
}