package com.thomaskuenneth.androidbuch.dbdemo3

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

val AUTHORITY = DBDemo3Provider::class.qualifiedName?.toLowerCase()
val CONTENT_URI = Uri.parse("content://AUTHORITY/mood")

class DBDemo3Provider : ContentProvider() {

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

}