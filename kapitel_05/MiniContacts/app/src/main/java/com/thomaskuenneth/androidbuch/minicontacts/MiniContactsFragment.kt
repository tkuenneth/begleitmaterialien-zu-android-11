package com.thomaskuenneth.androidbuch.minicontacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.ListFragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

class MiniContactsFragment : ListFragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val requestPermissionReadContacts = 123
    private val projection = arrayOf(
        ContactsContract.Data._ID,
        ContactsContract.Data.LOOKUP_KEY,
        ContactsContract.Data.DISPLAY_NAME
    )
    private val selection = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))"

    private lateinit var adapter: SimpleCursorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlePermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            requestPermissionReadContacts -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    load()
                } else {
                    setEmptyText(context?.getString(R.string.no_permission))
                }
            }
        }
    }

    override fun onCreateLoader(
        id: Int,
        args: Bundle?
    ): Loader<Cursor> {
        return CursorLoader(
            context!!,
            ContactsContract.Data.CONTENT_URI,
            projection, selection, null, null
        )
    }

    // Wird aufgerufen, wenn ein Loader mit dem Laden fertig ist
    override fun onLoadFinished(
        loader: Loader<Cursor>,
        data: Cursor?
    ) {
        adapter.swapCursor(data)
    }

    // Wird aufgerufen, wenn die Daten eines Loaders ungültig
    // geworden sind
    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter.swapCursor(null)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val intent = Intent(Intent.ACTION_VIEW)
        val c = listView.getItemAtPosition(position) as Cursor
        val uri = ContactsContract.Contacts.getLookupUri(
            c.getLong(0), c.getString(1)
        )
        intent.data = uri
        startActivity(intent)
    }

    private fun handlePermissions() {
        if (context?.checkSelfPermission(Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                requestPermissionReadContacts
            )
        } else {
            load()
        }
    }

    private fun load() {
        // Welche Spalte wird in welcher View angezeigt?
        val fromColumns =
            arrayOf(ContactsContract.Data.DISPLAY_NAME)
        val toViews = intArrayOf(android.R.id.text1)
        adapter = SimpleCursorAdapter(
            context,
            android.R.layout.simple_list_item_1, null,
            fromColumns, toViews, 0
        )
        listAdapter = adapter
        setEmptyText(context?.getString(R.string.no_contacts))
        // Loader initialisieren
        LoaderManager.getInstance(this).initLoader(0, null, this)
    }
}