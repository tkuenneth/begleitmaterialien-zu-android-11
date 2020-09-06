package com.thomaskuenneth.androidbuch.kontaktedemo1

import android.Manifest.permission.*
import android.content.pm.PackageManager.*
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_READ_CONTACTS = 123
class KontakteDemo1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(READ_CONTACTS)
                != PERMISSION_GRANTED) {
            requestPermissions(arrayOf(READ_CONTACTS),
                    REQUEST_READ_CONTACTS)
        } else {
            listContacts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS &&
                grantResults.isNotEmpty() && grantResults[0] ==
                PERMISSION_GRANTED) {
            listContacts()
        }
    }

    private fun listContacts() {
        // IDs und Namen aller sichtbaren Kontakte ermitteln
        val mainQueryProjection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME)
        val mainQuerySelection =
                "${ContactsContract.Contacts.IN_VISIBLE_GROUP} = ?"
        val mainQuerySelectionArgs = arrayOf("1")
        contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                mainQueryProjection,
                mainQuerySelection,
                mainQuerySelectionArgs, null)?.run {
            // Trefferliste abarbeiten...
            while (moveToNext()) {
                val contactId = getString(0)
                val displayName = getString(1)
                textview.append("===> $displayName ($contactId)\n")
            }
            close()
        }
    }
}