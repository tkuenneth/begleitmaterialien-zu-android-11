package com.thomaskuenneth.androidbuch.fragmentdemo3

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.*

class AuswahlFragment : ListFragment() {

    private val strLastSelected = "lastSelected"
    private var twoColumnMode = false
    private var lastSelected = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_activated_1,
            arrayOf("eins", "zwei", "drei")
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            lastSelected = savedInstanceState.getInt(strLastSelected, 0)
        }
    }

    override fun onListItemClick(l: ListView, v: View,
                                 position: Int, id: Long) {
        showDetails(position)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(strLastSelected, lastSelected)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        activity?.run {
            twoColumnMode =
                findViewById<ViewGroup>(R.id.container) != null
            if (twoColumnMode) {
                listView.choiceMode = ListView.CHOICE_MODE_SINGLE
                showDetails(lastSelected)
            } else {
                listView.choiceMode = ListView.CHOICE_MODE_NONE
            }
        }
    }

    private fun showDetails(index: Int) {
        lastSelected = index
        if (twoColumnMode) {
            listView.setItemChecked(index, true)
            fragmentManager?.run {
                var details =
                    findFragmentById(R.id.container) as DetailsFragment?
                if (details?.getIndex() ?: -1 != index) {
                    // neues Fragment passend zum selektierten
                    // Eintrag erzeugen und anzeigen
                    details = DetailsFragment()
                    val args = Bundle()
                    args.putInt(INDEX, index)
                    details.arguments = args
                    beginTransaction()
                        .replace(R.id.container, details)
                        // einen Übergang darstellen
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }
            }
        } else {
            val intent = Intent()
            intent.setClass(requireActivity(),
                DetailsActivity::class.java)
            intent.putExtra(INDEX, index)
            startActivity(intent)
        }
    }
}