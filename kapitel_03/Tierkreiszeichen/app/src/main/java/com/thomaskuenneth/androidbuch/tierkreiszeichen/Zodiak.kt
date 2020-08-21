package com.thomaskuenneth.androidbuch.tierkreiszeichen

import android.util.SparseArray
import java.util.*

private val tierkreis = createSparseArray()

private fun createSparseArray(): SparseArray<Tierkreiszeichen> {
    val a = SparseArray<Tierkreiszeichen>()
    a.put(Calendar.JANUARY, Tierkreiszeichen(
        21, Calendar.JANUARY, R.string.aquarius)
    )
    a.put(Calendar.FEBRUARY, Tierkreiszeichen(
        20, Calendar.FEBRUARY, R.string.pisces)
    )
    a.put(Calendar.MARCH, Tierkreiszeichen(
        21, Calendar.MARCH, R.string.aries)
    )
    a.put(Calendar.APRIL, Tierkreiszeichen(
        21, Calendar.APRIL, R.string.taurus)
    )
    a.put(Calendar.MAY, Tierkreiszeichen(
        22, Calendar.MAY, R.string.gemini)
    )
    a.put(Calendar.JUNE, Tierkreiszeichen(
        22, Calendar.JUNE, R.string.cancer)
    )
    a.put(Calendar.JULY, Tierkreiszeichen(
        24, Calendar.JULY, R.string.leo)
    )
    a.put(Calendar.AUGUST, Tierkreiszeichen(
        24, Calendar.AUGUST, R.string.virgo)
    )
    a.put(Calendar.SEPTEMBER, Tierkreiszeichen(
        24, Calendar.SEPTEMBER, R.string.libra)
    )
    a.put(Calendar.OCTOBER, Tierkreiszeichen(
        24, Calendar.OCTOBER, R.string.scorpius)
    )
    a.put(Calendar.NOVEMBER, Tierkreiszeichen(
        23, Calendar.NOVEMBER, R.string.sagittarius)
    )
    a.put(Calendar.DECEMBER, Tierkreiszeichen(
        22, Calendar.DECEMBER, R.string.capricornus)
    )
    return a
}

fun getTierkreiszeichenFuerMonat(
    monat: Int
): Tierkreiszeichen = tierkreis.get(monat)