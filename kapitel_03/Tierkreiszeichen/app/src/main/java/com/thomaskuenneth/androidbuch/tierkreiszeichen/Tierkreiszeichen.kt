package com.thomaskuenneth.androidbuch.tierkreiszeichen

import android.content.Context

class Tierkreiszeichen(
    val tag: Int, val monat: Int, private val tierkreiszeichen: Int
) {

    fun getName(context: Context): String {
        return context.getString(tierkreiszeichen)
    }

    val idForDrawable: Int
        get() = when (tierkreiszeichen) {
            R.string.aquarius -> R.drawable.aquarius
            R.string.aries -> R.drawable.aries
            R.string.cancer -> R.drawable.cancer
            R.string.capricornus -> R.drawable.capricornus
            R.string.gemini -> R.drawable.gemini
            R.string.leo -> R.drawable.leo
            R.string.libra -> R.drawable.libra
            R.string.pisces -> R.drawable.pisces
            R.string.sagittarius -> R.drawable.sagittarius
            R.string.scorpius -> R.drawable.scorpius
            R.string.taurus -> R.drawable.taurus
            R.string.virgo -> R.drawable.virgo
            else -> R.mipmap.ic_launcher
        }
}
