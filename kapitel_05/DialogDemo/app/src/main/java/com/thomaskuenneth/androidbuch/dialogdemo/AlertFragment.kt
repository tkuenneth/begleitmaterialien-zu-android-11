package com.thomaskuenneth.androidbuch.dialogdemo

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AlertFragment : DialogFragment() {

    private lateinit var listener: DialogInterface.OnClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DialogInterface.OnClickListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Builder instanziieren
        val builder = AlertDialog.Builder(requireContext())
        // Builder konfigurieren
        builder.setTitle(R.string.app_name)
        builder.setMessage(R.string.message)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.close, listener)
        // AlertDialog erzeugen und zurückliefern
        return builder.create()
    }

    companion object {
        val TAG = AlertFragment::class.simpleName
    }
}