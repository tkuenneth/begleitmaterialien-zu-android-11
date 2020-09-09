package com.thomaskuenneth.androidbuch.dbdemo3

import android.content.*
import android.database.*
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import java.util.*

val AUTHORITY =
    DBDemo3Provider::class.qualifiedName?.toLowerCase(Locale.US)
val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$TABLE_MOOD_NAME")
class DBDemo3Provider : ContentProvider() {
    private val mood = 1
    private val moodId = 2
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        uriMatcher.addURI(AUTHORITY, TABLE_MOOD_NAME, mood)
        uriMatcher.addURI(
            AUTHORITY, "$TABLE_MOOD_NAME/#",
            moodId
        )
    }

    private lateinit var dbHelper: DBDemo3OpenHelper

    override fun onCreate(): Boolean {
        dbHelper = DBDemo3OpenHelper(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String?>?, selection: String?,
        selectionArgs: Array<String?>?, sortOrder: String?
    ): Cursor? {
        val builder = SQLiteQueryBuilder()
        builder.tables = TABLE_MOOD_NAME // Ein bestimmer Eintrag?
        if (uriMatcher.match(uri) == moodId) {
            builder.appendWhere("$COLUMN_ID = ${uri.pathSegments[1]}")
        }
        val cursor = builder.query(
            dbHelper.writableDatabase, projection,
            selection, selectionArgs,
            null, null, if (sortOrder.isNullOrBlank()) MOOD_TIME else sortOrder
        )
        // bei Änderungen benachrichtigen
        context?.contentResolver.let {
            cursor?.setNotificationUri(it, uri)
        }
        return cursor
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val count = when (uriMatcher.match(uri)) {
            mood -> dbHelper.writableDatabase.update(
                TABLE_MOOD_NAME,
                values, selection, selectionArgs
            )
            moodId -> dbHelper.writableDatabase.update(
                TABLE_MOOD_NAME,
                values, "$COLUMN_ID = ${uri.pathSegments[1]}"
                        + if (!TextUtils.isEmpty(selection)) " AND ($selection)"
                else "",
                selectionArgs
            )
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        notifyChange(uri)
        return count
    }

    override fun delete(uri: Uri, selection: String?,
                        selectionArgs: Array<out String>?): Int {
        val count = when (uriMatcher.match(uri)) {
            mood -> dbHelper.writableDatabase.delete(
                TABLE_MOOD_NAME,
                selection, selectionArgs
            )
            moodId -> {
                dbHelper.writableDatabase.delete(
                    TABLE_MOOD_NAME,
                    "$COLUMN_ID = ${uri.pathSegments[1]}"
                            + if (!TextUtils.isEmpty(selection))
                        " AND ($selection)"
                    else "",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        notifyChange(uri)
        return count
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val rowID = dbHelper.writableDatabase
            .insert(
                TABLE_MOOD_NAME,
                "", values
            )
        if (rowID > 0) {
            val result = ContentUris.withAppendedId(
                CONTENT_URI,
                rowID
            )
            notifyChange(result)
            return result
        }
        throw SQLException("Failed to insert row into $uri")
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            mood ->
                "vnd.android.cursor.dir/vnd.$AUTHORITY/$TABLE_MOOD_NAME"
            moodId -> "vnd.android.cursor.item/vnd.$AUTHORITY/$TABLE_MOOD_NAME"
            else -> throw IllegalArgumentException(
                "Unsupported URI: "
                        + uri
            )
        }
    }

    private fun notifyChange(uri: Uri) {
        context?.contentResolver?.notifyChange(uri, null)
    }
}