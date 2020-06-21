package com.thomaskuenneth.androidbuch.rr

import android.util.Log
import java.io.File
import java.text.DateFormat
import java.util.*

const val EXT_3GP = ".3gp"
private val TAG = RRFile::class.simpleName
class RRFile(path: File?, name: String) : File(path, name) {

    override fun toString(): String {
        val lc = name.toLowerCase(Locale.US)
        try {
            val number = lc.substring(0, lc.indexOf(EXT_3GP))
            val d = Date(number.toLong())
            return DateFormat.getInstance().format(d)
        } catch (tr: Throwable) {
            Log.e(TAG, "Fehler beim Umwandeln oder Formatieren", tr)
        }
        return lc
    }
}