package com.thomaskuenneth.androidbuch.dbdemo3

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.ListFragment

class HistoryFragment : ListFragment() {
    private val menuInflater: MenuInflater?
        get() = activity?.menuInflater

    private lateinit var cursorAdapter: CursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cursorAdapter = DBDemo3Adapter(context)
        listAdapter = cursorAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerForContextMenu(listView)
        updateList()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater?.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item
            .menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.menu_good -> {
                update(
                    info.id,
                    MOOD_FINE
                )
                updateList()
                true
            }
            R.id.menu_ok -> {
                update(
                    info.id,
                    MOOD_OK
                )
                updateList()
                true
            }
            R.id.menu_bad -> {
                update(
                    info.id,
                    MOOD_BAD
                )
                updateList()
                true
            }
            R.id.menu_delete -> {
                delete(info.id)
                updateList()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun updateList() {
        val cursor = context?.contentResolver?.query(
            CONTENT_URI, null, null,
            null, "$MOOD_TIME DESC"
        )
        cursorAdapter.changeCursor(cursor)
    }

    private fun update(id: Long, mood: Int) {
        val uri = Uri.withAppendedPath(
            CONTENT_URI,
            id.toString()
        )
        val values = ContentValues()
        values.put(MOOD_MOOD, mood)
        context?.contentResolver?.update(uri, values, null, null)
    }

    private fun delete(id: Long) {
        val uri = Uri.withAppendedPath(
            CONTENT_URI,
            id.toString()
        )
        context?.contentResolver?.delete(uri, null, null)
    }
}