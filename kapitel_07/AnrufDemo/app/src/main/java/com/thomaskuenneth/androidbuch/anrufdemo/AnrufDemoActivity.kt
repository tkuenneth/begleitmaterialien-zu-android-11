package com.thomaskuenneth.androidbuch.anrufdemo

import android.Manifest
import android.content.*
import android.content.pm.*
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AnrufDemoActivity : AppCompatActivity() {
    private val requestCallPhone = 123
    private lateinit var buttonSofort: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonSofort = findViewById(R.id.sofort)
        buttonSofort.setOnClickListener {
            // sofort wählen
            val intent = Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:+49 (999) 44 55 66"))
            try {
                startActivity(intent)
            } catch (e: SecurityException) {
                Toast.makeText(this,
                        R.string.no_permission,
                        Toast.LENGTH_LONG).show()
            }
        }
        buttonSofort.isEnabled = if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),
                    requestCallPhone)
            false
        } else {
            true
        }
        val buttonDialog = findViewById<Button>(R.id.dialog)
        buttonDialog.setOnClickListener {
            // Wähldialog anzeigen
            val intent = Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:+49 (999) 44 55 66"))
            startActivity(intent)
        }
        val sms = findViewById<Button>(R.id.sms)
        sms.setOnClickListener {
            // SMS senden
            val telnr = "123-456-789"
            val smsUri = Uri.parse("smsto:$telnr")
            val sendIntent = Intent(Intent.ACTION_SENDTO,
                    smsUri)
            sendIntent.putExtra("sms_body",
                    "Hier steht der Text der Nachricht...")
            startActivity(sendIntent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == requestCallPhone &&
                (grantResults.isNotEmpty()
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED)) {
            buttonSofort.isEnabled = true
        }
    }
}