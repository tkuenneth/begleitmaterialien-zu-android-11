package com.thomaskuenneth.androidbuch.dialogdemo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment() {

    private lateinit var listener: OnDateSetListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDateSetListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // das aktuelle Datum als Voreinstellung nehmen
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        // einen DatePickerDialog erzeugen und zurückliefern
        return DatePickerDialog(
            requireContext(),
            listener, year, month, day
        )
    }

    companion object {
        val TAG = DatePickerFragment::class.simpleName
    }
}