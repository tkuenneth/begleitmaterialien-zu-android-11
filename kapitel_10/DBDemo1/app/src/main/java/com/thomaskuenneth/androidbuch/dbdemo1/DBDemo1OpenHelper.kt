package com.thomaskuenneth.androidbuch.dbdemo1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Konstanten für die Stimmungen
const val MOOD_FINE = 1
const val MOOD_OK = 2
const val MOOD_BAD = 3

// Name und Version der Datenbank
private const val DATABASE_NAME = "tkmoodley.db"
private const val DATABASE_VERSION = 1

private val TAG = DBDemo1OpenHelper::class.simpleName
class DBDemo1OpenHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Name und Attribute der Tabelle "mood"
    private val id = "_id"
    private val tableMoodName = "mood"
    private val moodTime = "timeMillis"
    private val moodMood = "mood"

    // Tabelle "mood" anlegen
    private val tableMoodCreate = """
    CREATE TABLE $tableMoodName (
    $id INTEGER PRIMARY KEY AUTOINCREMENT,
    $moodTime INTEGER,
    $moodMood INTEGER);")    
  """.trimIndent()

    // Tabelle "mood" löschen
    private val tableMoodDrop = "DROP TABLE IF EXISTS $tableMoodName"

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

    fun insert(mood: Int, timeMillis: Long) {
        var rowId = -1L
        try {
            // Datenbank öffnen
            Log.d(TAG, "Pfad: " + writableDatabase.path)
            // die zu speichernden Werte
            val values = ContentValues()
            values.put(moodMood, mood)
            values.put(moodTime, timeMillis)
            // in die Tabelle "mood" einfügen
            rowId = writableDatabase.insert(tableMoodName, null, values)
        } catch (e: SQLiteException) {
            Log.e(TAG, "insert()", e)
        } finally {
            Log.d(TAG, "insert(): rowId=$rowId")
        }
    }
}