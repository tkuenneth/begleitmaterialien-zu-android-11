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

private const val REQUEST_FINE_LOCATION = 123
class LocationDemo1Activity : AppCompatActivity() {
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

        override fun onLocationChanged(loc: Location) {
            tv.append("\nonLocationChanged()\n")
            tv.append("Breite: ${loc.latitude}\nLänge: ${loc.longitude}\n")
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
                REQUEST_FINE_LOCATION
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
        if (requestCode == REQUEST_FINE_LOCATION &&
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
            val p = manager.getProvider(name)
            tv.append(" --> requiresCell(): ${p?.requiresCell()}\n")
            tv.append(" --> requiresNetwork(): ${p?.requiresNetwork()}\n")
            tv.append(" --> requiresSatellite(): ${p?.requiresSatellite()}\n")
        }
        // Provider mit grober Auflösung
        // und niedrigem Energieverbrauch
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_COARSE
        criteria.powerRequirement = Criteria.POWER_LOW
        provider = manager.getBestProvider(criteria, true) ?: ""
        tv.append("\nVerwende $provider\n")
        val loc = Location(LocationManager.GPS_PROVIDER)
        loc.latitude = Location.convert("49:27")
        loc.longitude = Location.convert("11:5")
        tv.append("latitude: ${loc.latitude}\nlongitude: ${loc.longitude}\n")
    }
}