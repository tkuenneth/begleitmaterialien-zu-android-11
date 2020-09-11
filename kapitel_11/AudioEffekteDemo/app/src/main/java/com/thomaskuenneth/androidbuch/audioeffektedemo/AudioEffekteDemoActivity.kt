package com.thomaskuenneth.androidbuch.audioeffektedemo

import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.media.audiofx.BassBoost
import android.media.audiofx.PresetReverb
import android.media.audiofx.Virtualizer
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private val TAG = AudioEffekteDemoActivity::class.simpleName
class AudioEffekteDemoActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null
    private var reverb: PresetReverb? = null
    private var playing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // MediaPlayer instanziieren
        mediaPlayer = MediaPlayer.create(this, R.raw.guten_tag)
        mediaPlayer?.setOnCompletionListener {
            playing = false
            updateButtonText()
        }
        val sessionId = mediaPlayer?.audioSessionId
        // BassBoost instanziieren und an Audio Session binden
        bassBoost = BassBoost(0, sessionId ?: 0)
        Log.d(TAG, "roundedStrength: ${bassBoost?.roundedStrength}")
        if (bassBoost?.strengthSupported == true) {
            bassBoost?.setStrength(1000.toShort())
        }
        // Checkbox schaltet BassBoost aus und ein
        cbBassBoost.setOnCheckedChangeListener { _: CompoundButton?,
                                                 isChecked: Boolean ->
            val result = bassBoost?.setEnabled(isChecked)
            if (result != AudioEffect.SUCCESS) {
                Log.e(TAG, "Bass Boost: setEnabled($isChecked) = $result")
            }
        }
        cbBassBoost.isChecked = false
        // Virtualizer instanziieren und an Audio Session binden
        virtualizer = Virtualizer(0, sessionId ?: 0)
        virtualizer?.setStrength(1000.toShort())
        // Checkbox schaltet Virtualizer aus und ein
        cbVirtualizer.setOnCheckedChangeListener { _: CompoundButton?,
                                                   isChecked: Boolean ->
            val result = virtualizer?.setEnabled(isChecked)
            if (result != AudioEffect.SUCCESS) {
                Log.e(TAG, "Virtualizer: setEnabled($isChecked) = $result")
            }
        }
        cbVirtualizer.isChecked = false
        // Hall
        reverb = PresetReverb(0, 0)
        reverb?.preset = PresetReverb.PRESET_PLATE
        reverb?.id?.let {
            mediaPlayer?.attachAuxEffect(it)
        }
        mediaPlayer?.setAuxEffectSendLevel(1f)
        // Checkbox schaltet Hall aus und ein
        cbReverb.setOnCheckedChangeListener { _: CompoundButton?,
                                              isChecked: Boolean ->
            val result = reverb?.setEnabled(isChecked)
            if (result != AudioEffect.SUCCESS) {
                Log.e(TAG, "PresetReverb: setEnabled($isChecked) = $result")
            }
        }
        cbReverb.isChecked = false
        // Schaltfläche
        button.setOnClickListener {
            if (playing) {
                mediaPlayer?.pause()
            } else {
                mediaPlayer?.start()
            }
            playing = !playing
            updateButtonText()
        }
        playing = false
        updateButtonText()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        bassBoost?.release()
        virtualizer?.release()
        reverb?.release()
        mediaPlayer?.release()
    }

    private fun updateButtonText() {
        button.text = getString(if (playing) R.string.stop
        else R.string.start)
    }
}