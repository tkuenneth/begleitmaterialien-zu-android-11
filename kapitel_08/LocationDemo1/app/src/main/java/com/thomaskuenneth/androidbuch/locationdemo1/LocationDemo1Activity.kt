package com.thomaskuenneth.androidbuch.locationdemo1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class LocationDemo1Activity : AppCompatActivity() {
    private val requestFineLocation = 123
    private val listener = object : LocationListener {
        override fun onStatusChanged(
            provider: String, status: Int,
            extras: Bundle
        ) {
            tv.append("onStatusChanged()\n")
        }

        override fun onProviderEnabled(provider: String) {
            tv.append("onProviderEnabled()\n")
        }

        override fun onProviderDisabled(provider: String) {
            tv.append("onProviderDisabled()\n")
        }

        override fun onLocationChanged(location: Location?) {
            tv.append("\nonLocationChanged()\n")
            if (location != null) {
                tv.append("Breite: ${location.latitude}\nLänge: ${location.longitude}\n")
            }
        }
    }

    private lateinit var manager: LocationManager
    private lateinit var provider: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.text = ""
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestFineLocation
            )
        } else {
            doIt()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestFineLocation &&
            grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            doIt()
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                manager.requestLocationUpdates(provider, 3000L, 0.0f, listener)
            } catch (t: Throwable) {
                tv.append("${t.message}\n")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            manager.removeUpdates(listener)
        }
    }

    private fun doIt() {
        manager = getSystemService(LocationManager::class.java)
        // Liste mit Namen aller Provider ausgeben
        val providers: List<String> = manager.allProviders
        for (name in providers) {
            val enabled: Boolean = manager.isProviderEnabled(name)
            tv.append("Name: $name\n")
            tv.append(" --> isProviderEnabled(): $enabled\n")
            if (!enabled) {
                continue
            }
            val locationProvider = manager.getProvider(name)
            tv.append(" --> requiresCell(): ${locationProvider?.requiresCell()}\n")
            tv.append(" --> requiresNetwork(): ${locationProvider?.requiresNetwork()}\n")
            tv.append(" --> requiresSatellite(): ${locationProvider?.requiresSatellite()}\n")
        }
        // Provider mit grober Auflösung
        // und niedrigem Energieverbrauch
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_COARSE
        criteria.powerRequirement = Criteria.POWER_LOW
        provider = manager.getBestProvider(criteria, true) ?: ""
        tv.append("\nVerwende $provider\n")
        val locNuernberg = Location(
            LocationManager.GPS_PROVIDER
        )
        val latitude = Location.convert("49:27")
        locNuernberg.latitude = latitude
        val longitude = Location.convert("11:5")
        locNuernberg.longitude = longitude
        tv.append("latitude: ${locNuernberg.latitude}\nlongitude: ${locNuernberg.longitude}\n")
    }
}