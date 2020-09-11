package com.thomaskuenneth.androidbuch.texttospeechdemo

import android.content.Intent
import android.os.*
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

private const val CHECK_TTS_DATA = 1
private val TAG = TextToSpeechDemoActivity::class.simpleName
class TextToSpeechDemoActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private val supportedLanguages = Hashtable<String, Locale>()
    private var tts: TextToSpeech? = null
    private var lastUtteranceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // die Sprachsynthesekomponente wurde
        // noch nicht initialisiert
        tts = null
        // prüfen, ob Sprachpakete vorhanden sind
        val intent = Intent()
        intent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
        startActivityForResult(intent, CHECK_TTS_DATA)
    }

    // ggf. Ressourcen freigeben
    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // Sind Sprachpakete vorhanden?
        if (requestCode == CHECK_TTS_DATA) {
            if (resultCode ==
                TextToSpeech.Engine.CHECK_VOICE_DATA_PASS
            ) {
                // Initialisierung der Sprachkomponente starten
                tts = TextToSpeech(this, this)
            } else {
                // Installation der Sprachpakete vorbereiten
                val installIntent = Intent()
                installIntent.action =
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                startActivity(installIntent)
                // Activity beenden
                finish()
            }
        }
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            // die Initialisierung war nicht erfolgreich
            finish()
        }
        Log.d(TAG, "Standard: ${tts?.defaultEngine}")
        tts?.engines?.forEach {
            Log.d(TAG, "${it.label} (${it.name})")
        }
        // Activity initialisieren
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            val text = input.text.toString()
            val key = spinner.selectedItem as String
            supportedLanguages[key]?.let {
                button.isEnabled = false
                tts?.language = it
                lastUtteranceId = System
                    .currentTimeMillis().toString()
                tts?.speak(
                    text, TextToSpeech.QUEUE_FLUSH,
                    null, lastUtteranceId
                )
                // in Datei schreiben
                val file = File(filesDir, "${lastUtteranceId}.wav")
                tts?.synthesizeToFile(text, null, file, lastUtteranceId)
                Log.d(TAG, file.absolutePath)
            }
        }
        tts?.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) {
                    Log.d(TAG, "onStart(): $utteranceId")
                }

                override fun onDone(utteranceId: String) {
                    Log.d(TAG, "onDone(): $utteranceId")
                    if (utteranceId == lastUtteranceId) {
                        Handler(Looper.getMainLooper()).post {
                            button.isEnabled = true
                        }
                    }
                }

                override fun onError(utteranceId: String) {
                    Log.d(TAG, "onError(): $utteranceId")
                }
            })
        // Liste der Sprachen ermitteln
        val languages = Locale.getISOLanguages()
        for (lang in languages) {
            val loc = Locale(lang)
            when (tts?.isLanguageAvailable(loc)) {
                TextToSpeech.LANG_MISSING_DATA,
                TextToSpeech.LANG_NOT_SUPPORTED -> {
                    Log.d(TAG, "language not available for $loc")
                }
                else -> {
                    val key = loc.displayLanguage
                    if (!supportedLanguages.containsKey(key)) {
                        supportedLanguages[key] = loc
                    }
                }
            }
        }
        val adapter = ArrayAdapter<Any>(
            this,
            android.R.layout.simple_spinner_item, supportedLanguages
                .keys.toTypedArray()
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner.adapter = adapter
    }
}