package com.thomaskuenneth.androidbuch.dbdemo2

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.ListFragment

class HistoryFragment : ListFragment() {

    private val menuInflater: MenuInflater?
        get() = activity?.menuInflater

    private lateinit var cursorAdapter: CursorAdapter
    private lateinit var dbHelper: DBDemo2OpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cursorAdapter = DBDemo2Adapter(context)
        dbHelper = DBDemo2OpenHelper(context)
        listAdapter = cursorAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
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
                dbHelper.update(
                    info.id,
                    MOOD_FINE
                )
                updateList()
                true
            }
            R.id.menu_ok -> {
                dbHelper.update(
                    info.id,
                    MOOD_OK
                )
                updateList()
                true
            }
            R.id.menu_bad -> {
                dbHelper.update(
                    info.id,
                    MOOD_BAD
                )
                updateList()
                true
            }
            R.id.menu_delete -> {
                dbHelper.delete(info.id)
                updateList()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun updateList() {
        // Cursor tauschen - der alte wird geschlossen
        cursorAdapter.changeCursor(dbHelper.query())
    }
}