package com.thomaskuenneth.androidbuch.locationdemo2

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class LocationDemo2Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val requestFineLocation = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == requestFineLocation &&
            grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            markerDemo()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestFineLocation
            )
        } else {
            markerDemo()
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val berlin = LatLng(
            Location.convert("52:31:12"),
            Location.convert("13:24:36")
        )
        val cu1 = CameraUpdateFactory.newLatLngZoom(berlin, 8f)
        mMap.moveCamera(cu1)

        val nuernberg = LatLng(
            Location.convert("49:27:20"),
            Location.convert("11:04:43")
        )
        val cameraPosition = CameraPosition.Builder()
            .target(nuernberg)
            .zoom(17f)
            .bearing(90f)
            .tilt(30f)
            .build()
        val cu3 = CameraUpdateFactory.newCameraPosition(cameraPosition)
        mMap.animateCamera(cu3, 5000, null)
    }

    @Throws(SecurityException::class)
    private fun markerDemo() {
        val options = MarkerOptions()
        val m = getSystemService(LocationManager::class.java)
        // Hier könnte eine SecurityException geworfen werden
        val loc: Location? = m.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        if (loc != null) {
            val pos = LatLng(loc.latitude, loc.longitude)
            options.position(pos)
            options.icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE
                )
            )
            mMap.addMarker(options)
        }
    }
}