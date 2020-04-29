package com.thomaskuenneth.androidbuch.fragmentdemo3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.ListFragment

class AuswahlFragment : ListFragment() {

    private val strZuletztSelektiert = "zuletztSelektiert"
    private var zweiSpaltenModus = false
    private var zuletztSelektiert = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_list_item_activated_1,
            arrayOf("eins", "zwei", "drei")
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            zuletztSelektiert = savedInstanceState.getInt(strZuletztSelektiert, 0)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        detailsAnzeigen(position)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(strZuletztSelektiert, zuletztSelektiert)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        zweiSpaltenModus = activity?.findViewById<ViewGroup>(R.id.container) != null
        if (zweiSpaltenModus) {
            listView.choiceMode = ListView.CHOICE_MODE_SINGLE
            detailsAnzeigen(zuletztSelektiert)
        } else {
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }
    }

    private fun detailsAnzeigen(index: Int) {
        zuletztSelektiert = index
        if (zweiSpaltenModus) {
            listView.setItemChecked(index, true)
            var details = fragmentManager?.findFragmentById(R.id.container) as DetailsFragment?
            if (details == null || details.getIndex() != index) {
                // neues Fragment passend zum selektierten
                // Eintrag erzeugen und anzeigen
                details = DetailsFragment()
                val args = Bundle()
                args.putInt(INDEX, index)
                details.arguments = args
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.container, details)
                    // einen Übergang darstellen
                    ?.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_FADE
                    )
                    ?.commit()
            }
        } else {
            val intent = Intent()
            intent.setClass(activity!!, DetailsActivity::class.java)
            intent.putExtra(INDEX, index)
            startActivity(intent)
        }
    }
}