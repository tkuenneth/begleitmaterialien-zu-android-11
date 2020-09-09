package com.thomaskuenneth.androidbuch.dbdemo2

import android.content.Context
import android.database.Cursor
import android.view.*
import android.widget.*
import java.text.*
import java.util.*

class DBDemo2Adapter(context: Context?) : CursorAdapter(context, null,
    0) {
    private val dateFormat = SimpleDateFormat
        .getDateInstance(DateFormat.MEDIUM)
    private val timeFormat = SimpleDateFormat
        .getTimeInstance(DateFormat.MEDIUM)

    private val inflater = LayoutInflater.from(context)
    private val date = Date()

    override fun newView(context: Context?, cursor: Cursor?,
                         parent: ViewGroup?): View {
        return inflater.inflate(R.layout.icon_text_text, null)
    }

    override fun bindView(view: View?, context: Context?,
                          cursor: Cursor?) {
        cursor ?: return
        val ciMood = cursor.getColumnIndex(MOOD_MOOD)
        val mood = cursor.getInt(ciMood)
        val image = view?.findViewById<ImageView>(R.id.icon)
        image?.setImageResource(
            when (mood) {
                MOOD_FINE -> R.drawable.ic_smiley_fine
                MOOD_OK -> R.drawable.ic_smiley_ok
                else -> R.drawable.ic_smiley_bad
            }
        )
        val ciTimeMillis = cursor.getColumnIndex(MOOD_TIME)
        val timeMillis = cursor.getLong(ciTimeMillis)
        date.time = timeMillis
        val textview1 = view?.findViewById<TextView>(R.id.text1)
        textview1?.text = dateFormat.format(date)
        val textview2 = view?.findViewById<TextView>(R.id.text2)
        textview2?.text = timeFormat.format(date)
    }
}