package com.thomaskuenneth.androidbuch.kontaktedemo2

import android.Manifest.permission.*
import android.content.*
import android.content.pm.PackageManager.*
import android.os.Bundle
import android.provider.ContactsContract.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.*
import java.util.*

private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
private const val PERMISSIONS_REQUEST_READ_CONTACTS = 123
class KontakteDemo2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(READ_CONTACTS) != PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(READ_CONTACTS, WRITE_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            doIt()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS &&
            grantResults.size == 2 &&
            grantResults[0] == PERMISSION_GRANTED &&
            grantResults[1] == PERMISSION_GRANTED
        ) {
            doIt()
        }
    }

    private fun doIt() {
        // nach "Testperson" suchen
        val mainQueryProjection = arrayOf(Contacts._ID)
        val mainQuerySelection = """
            ${Contacts.IN_VISIBLE_GROUP} = ? AND 
            ${Contacts.DISPLAY_NAME} IS ?
        """.cleanup()
        val mainQuerySelectionArgs = arrayOf("1", "Testperson")
        contentResolver.query(
            Contacts.CONTENT_URI,
            mainQueryProjection,
            mainQuerySelection, mainQuerySelectionArgs, null
        )?.run {
            if (moveToNext()) {
                val contactId = getString(0)
                output("===> Testperson gefunden ($contactId)")
                updateOrInsertBirthday(contentResolver, contactId)
            } else {
                output("Testperson nicht gefunden")
            }
            close()
        }
    }

    private fun updateOrInsertBirthday(
        contentResolver: ContentResolver,
        contactId: String
    ) {
        val dataQueryProjection = arrayOf(
            CommonDataKinds.Event._ID,
            CommonDataKinds.Event.START_DATE
        )
        val dataQuerySelection = """
            ${Data.CONTACT_ID} = ? AND
            ${Data.MIMETYPE} = ? AND
            ${CommonDataKinds.Event.TYPE} = ?
        """.cleanup()
        val dataQuerySelectionArgs = arrayOf(
            contactId,
            CommonDataKinds.Event.CONTENT_ITEM_TYPE,
            CommonDataKinds.Event.TYPE_BIRTHDAY.toString()
        )
        // Gibt es einen Geburtstag zu Kontakt #contactId?
        contentResolver.query(
            Data.CONTENT_URI, dataQueryProjection,
            dataQuerySelection, dataQuerySelectionArgs, null
        )?.run {
            if (moveToNext()) {
                // ja, Eintrag gefunden
                val dataId = getString(0)
                var date = getString(1)
                output("Geburtstag (_id=$dataId): $date")
                // Jahr um 1 verringern
                try {
                    DATE_FORMAT.parse(date)?.let { d ->
                        val cal = Calendar.getInstance()
                        cal.time = d
                        cal.add(Calendar.YEAR, -1)
                        date = DATE_FORMAT.format(cal.time)
                        output("neues Geburtsdatum: $date")
                    }
                    // Tabelle aktualisieren
                    val updateWhere = """
                        ${CommonDataKinds.Event._ID} = ? AND
                        ${Data.MIMETYPE} = ? AND
                        ${CommonDataKinds.Event.TYPE} = ?
                    """.cleanup()
                    val updateSelectionArgs = arrayOf(
                        dataId,
                        CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                        CommonDataKinds.Event.TYPE_BIRTHDAY.toString()
                    )
                    val values = ContentValues()
                    values.put(
                        CommonDataKinds.Event.START_DATE,
                        date
                    )
                    val numRows = contentResolver.update(
                        Data.CONTENT_URI, values,
                        updateWhere, updateSelectionArgs
                    )
                    output("update() war ${
                        if (numRows == 0) "nicht " else ""}erfolgreich")
                } catch (e: ParseException) {
                    output(e.toString())
                }
            } else {
                output("keinen Geburtstag gefunden")
                // Strings für die Suche nach RawContacts
                val rawProjection = arrayOf(RawContacts._ID)
                val rawSelection = "${RawContacts.CONTACT_ID} = ?"
                val rawSelectionArgs = arrayOf(contactId)
                // Werte für Tabellenzeile vorbereiten
                val values = ContentValues()
                values.put(
                    CommonDataKinds.Event.START_DATE,
                    DATE_FORMAT.format(Date())
                )
                values.put(
                    Data.MIMETYPE,
                    CommonDataKinds.Event.CONTENT_ITEM_TYPE
                )
                values.put(
                    CommonDataKinds.Event.TYPE,
                    CommonDataKinds.Event.TYPE_BIRTHDAY
                )
                // alle RawContacts befüllen
                contentResolver.query(
                    RawContacts.CONTENT_URI,
                    rawProjection, rawSelection,
                    rawSelectionArgs, null
                )?.run {
                    while (moveToNext()) {
                        val rawContactId = getString(0)
                        values.put(
                            CommonDataKinds.Event.RAW_CONTACT_ID,
                            rawContactId
                        )
                        val uri = contentResolver.insert(
                            Data.CONTENT_URI,
                            values
                        )
                        output(
                            """
                            ---> Hinzufügen des Geburtstags
                            für RawContacts-Id $rawContactId war
                            ${if (uri == null) "nicht" else ""} erfolgreich
                        """.cleanup()
                        )
                    }
                    close()
                }
            }
            close()
        }
    }

    private fun output(s: String) {
        textview.append("$s\n")
    }
    
    private fun String.cleanup(): String = trimIndent().replace("\n", " ")
}