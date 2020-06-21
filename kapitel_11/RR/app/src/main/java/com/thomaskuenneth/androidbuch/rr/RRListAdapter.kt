package com.thomaskuenneth.androidbuch.rr

import android.content.Context
import android.widget.ArrayAdapter
import java.io.File
import java.util.*

class RRListAdapter(context: Context) :
    ArrayAdapter<File>(context, android.R.layout.simple_list_item_1) {

    init {
        findAndAddFiles()
    }

    private fun findAndAddFiles() {
        val files = getBaseDir(context)?.listFiles { dir: File?, filename: String ->
            if (filename.toLowerCase(Locale.US).endsWith(EXT_3GP)) {
                val f = File(dir, filename)
                f.canRead() && !f.isDirectory
            } else
                false
        }
        files?.forEach {
            add(RRFile(it.parentFile, it.name))
        }
    }
}