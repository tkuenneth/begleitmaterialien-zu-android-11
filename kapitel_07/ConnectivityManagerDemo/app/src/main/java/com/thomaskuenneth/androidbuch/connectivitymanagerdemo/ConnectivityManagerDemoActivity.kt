package com.thomaskuenneth.androidbuch.connectivitymanagerdemo

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class ConnectivityManagerDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mgr = getSystemService(ConnectivityManager::class.java)
        mgr?.allNetworks?.forEach {
            val properties = mgr.getLinkProperties(it)
            textview.append("${properties?.interfaceName}\n")
            val capabilities = mgr.getNetworkCapabilities(it)
            val notRoaming = capabilities?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING) ?: true
            textview.append("Roaming ist ${if (notRoaming) "aus"
            else "ein"}\n")
            // ab API-Level 29 vorhanden
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                textview.append("Signalstärke: ${
                    capabilities?.signalStrength}\n")
            }
            val foreground = capabilities?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_FOREGROUND)
                ?: false
            textview.append("Nutzbar durch Apps: $foreground\n\n")
        }
    }
}