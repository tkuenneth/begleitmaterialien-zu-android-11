package com.thomaskuenneth.androidbuch.kontaktedemo1

import android.Manifest.permission.*
import android.content.pm.PackageManager.*
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.*
import android.provider.ContactsContract.Data
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
//import java.util.*
//import java.util.regex.*
//import java.text.*
//import android.util.*

//private val FORMAT_YYYYMMDD = SimpleDateFormat("yyyyMMdd", Locale.US)
//private val TAG = KontakteDemo1Activity::class.simpleName
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
        else
            textview.text = getString(R.string.no_permission)
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
                infosAuslesen(contactId)
            }
            close()
        }
    }

    private fun infosAuslesen(contactId: String) {
        val dataQueryProjection = arrayOf( Event.TYPE, Event.START_DATE,
                Event.LABEL)
        val dataQuerySelection =
                "${Data.CONTACT_ID} = ? AND ${Data.MIMETYPE} = ?"
        val dataQuerySelectionArgs = arrayOf(contactId,
                Event.CONTENT_ITEM_TYPE)
        contentResolver.query(Data.CONTENT_URI, dataQueryProjection,
                dataQuerySelection, dataQuerySelectionArgs,
                null)?.run {
            while (moveToNext()) {
                val type = getInt(0)
                val label = getString(2)
                if (Event.TYPE_BIRTHDAY == type) {
                    val stringBirthday = getString(1)
                    textview.append("_____birthday: $stringBirthday\n")
                } else {
                    val stringAnniversary = getString(1)
                    textview.append(
                            "_____event: $stringAnniversary (type=$type, label=$label)")
                    when {
                        Event.TYPE_ANNIVERSARY == type -> {
                            textview.append("_____TYPE_ANNIVERSARY\n")
                        }
                        Event.TYPE_CUSTOM == type -> {
                            textview.append("_____TYPE_CUSTOM\n")
                        }
                        else -> {
                            textview.append("_____TYPE_OTHER\n")
                        }
                    }
                }
            }
            close()
        }
    }

//    fun getDateFromString1(string: String): Date {
//        val p = Pattern.compile("(\\d\\d\\d\\d).*(\\d\\d).*(\\d\\d)",
//                Pattern.DOTALL)
//        val m = p.matcher(string)
//        if (m.matches()) {
//            val date = "${m.group(1)}${m.group(2)}${m.group(3)}"
//            try {
//                return FORMAT_YYYYMMDD.parse(date) ?: Date()
//            } catch (tr: Throwable) {
//                Log.e(TAG, "getDateFromString1()", tr)
//            }
//        }
//        return Date()
//    }
}