package com.thomaskuenneth.androidbuch.dialogdemo

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AlertFragment : DialogFragment() {

    private lateinit var l: DialogInterface.OnClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DialogInterface.OnClickListener) {
            l = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Builder instanziieren
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        // Builder konfigurieren
        builder.setTitle(R.string.app_name)
        builder.setMessage(R.string.message)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.close, l)
        // AlertDialog erzeugen und zurückliefern
        return builder.create()
    }

    companion object {
        val TAG = AlertFragment::class.simpleName
    }
}