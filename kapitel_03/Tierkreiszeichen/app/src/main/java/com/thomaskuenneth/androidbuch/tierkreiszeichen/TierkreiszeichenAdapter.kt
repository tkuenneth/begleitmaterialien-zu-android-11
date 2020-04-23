package com.thomaskuenneth.androidbuch.tierkreiszeichen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TierkreiszeichenAdapter(context: Context) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val zodiak = ArrayList<Tierkreiszeichen>()
    private val cal = Calendar.getInstance()

    // Legt fest, in welchem Format das Datum ausgegeben wird
    private val df: DateFormat = SimpleDateFormat(
        context.getString(R.string.format_string),
        Locale.US
    )

    init {
        // Tierkreiszeichen für alle Monate ermitteln
        for (monat in Calendar.JANUARY..Calendar.DECEMBER) {
            val zeichen = getTierkreiszeichenFuerMonat(monat)
            zodiak.add(zeichen)
        }
    }

    override fun getCount(): Int {
        return zodiak.size
    }

    override fun getItem(position: Int): Any {
        return zodiak[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?, parent: ViewGroup
    ): View {
        var currentPosition = position
        val view: View
        val holder: ViewHolder
        // falls nötig, convertView bauen
        if (convertView == null) {
            // Layoutdatei entfalten
            view = inflater.inflate(
                R.layout.icon_text_text,
                parent, false
            )
            // Holder erzeugen
            holder = ViewHolder()
            holder.name = view.findViewById(R.id.text1)
            holder.datumsbereich = view
                .findViewById(R.id.text2)
            holder.icon = view.findViewById(R.id.icon)
            view.tag = holder
        } else {
            // Holder bereits vorhanden
            holder = convertView.tag as ViewHolder
            view = convertView
        }
        val context: Context = parent.context
        var zeichen = getItem(currentPosition) as Tierkreiszeichen
        holder.name?.text = zeichen.getName(context)
        holder.icon?.setImageResource(zeichen.idForDrawable)
        cal.set(Calendar.DAY_OF_MONTH, zeichen.tag)
        cal.set(Calendar.MONTH, zeichen.monat)
        val datum1: String = df.format(cal.time)
        if (++currentPosition >= count) {
            currentPosition = 0
        }
        zeichen = getItem(currentPosition) as Tierkreiszeichen
        cal.set(Calendar.DAY_OF_MONTH, zeichen.tag - 1)
        cal.set(Calendar.MONTH, zeichen.monat)
        val datum2: String = df.format(cal.time)
        holder.datumsbereich?.text = context.getString(
            R.string.interval,
            datum1, datum2
        )
        return view
    }
}

private class ViewHolder {
    var name: TextView? = null
    var datumsbereich: TextView? = null
    var icon: ImageView? = null
}
