package com.thomaskuenneth.androidbuch.dbdemo3

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Konstanten für die Stimmungen
const val MOOD_FINE = 1
const val MOOD_OK = 2
const val MOOD_BAD = 3

// Spalten werden in DBDemo2Adapter verwendet
const val MOOD_TIME = "timeMillis"
const val MOOD_MOOD = "mood"

// Name und Attribute der Tabelle "mood"
const val TABLE_MOOD_NAME = "mood"
const val COLUMN_ID = "_id"

// Name und Version der Datenbank
private const val DATABASE_NAME = "tkmoodley.db"
private const val DATABASE_VERSION = 1

private val TAG = DBDemo3OpenHelper::class.simpleName
class DBDemo3OpenHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Tabelle "mood" anlegen
    private val tableMoodCreate = """
        CREATE TABLE $TABLE_MOOD_NAME (
        $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $MOOD_TIME INTEGER,
        $MOOD_MOOD INTEGER);")        
    """.trimIndent()

    // Tabelle "mood" löschen
    private val tableMoodDrop = "DROP TABLE IF EXISTS $TABLE_MOOD_NAME"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(tableMoodCreate)
    }

    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int,
        newVersion: Int
    ) {
        Log.w(
            TAG, """
                Upgrade der Datenbank von Version $oldVersion zu $newVersion.
                Alle Daten werden gelöscht."                
            """.trimIndent()
        )
        db.execSQL(tableMoodDrop)
        onCreate(db)
    }
}