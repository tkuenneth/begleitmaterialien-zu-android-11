package com.thomaskuenneth.androidbuch.biometricprompt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import kotlinx.android.synthetic.main.activity_main.*

class BiometricPromptDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { showDialog() }
    }

    private fun toast(resid: Int) = Toast.makeText(this, resid, Toast.LENGTH_LONG).show()

    private fun showDialog() {
        val info = BiometricPrompt.PromptInfo.Builder()
                .setDescription(getString(R.string.descr))
                .setTitle(getString(R.string.title))
                .setConfirmationRequired(true)
                .setSubtitle(getString(R.string.subtitle))
                .setNegativeButtonText(getString(R.string.cancel))
                .build()
        BiometricPrompt(this, mainExecutor,
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
                }).authenticate(info)
    }
}