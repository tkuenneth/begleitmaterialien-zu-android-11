package com.thomaskuenneth.androidbuch.biometricprompt

import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class BiometricPromptDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prompt = BiometricPrompt.Builder(this)
                .setDescription(getString(R.string.descr))
                .setTitle(getString(R.string.title))
                .setSubtitle(getString(R.string.subtitle))
                .setNegativeButton(getString(R.string.button),
                        mainExecutor, { _: DialogInterface?, _: Int -> toast(R.string.clicked) }
                ).build()
        prompt.authenticate(CancellationSignal(), mainExecutor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        toast(R.string.error)
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        toast(R.string.ok)
                    }

                    override fun onAuthenticationFailed() {
                        toast(R.string.failed)
                    }
                })
    }

    private fun toast(resid: Int) {
        Toast.makeText(this, resid, Toast.LENGTH_LONG).show()
        textview.setText(resid)
    }
}