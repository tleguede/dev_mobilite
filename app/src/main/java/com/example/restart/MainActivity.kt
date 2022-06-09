package com.example.restart

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.IOException


class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var mMap: GoogleMap? = null
    internal lateinit var mLastLocation: Location
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    lateinit var dialog: BottomSheetDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val view: View = layoutInflater.inflate(R.layout.item_bottom_sheet, null);
        val dialog: BottomSheetDialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showResultList() {
        val view: View = layoutInflater.inflate(R.layout.item_bottom_sheet, null);
        val dialog: BottomSheetDialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        dialog.show()


//        dialog.setOnShowListener {
//            val list = arrayOf("first", "second");
//            val listView = findViewById<ListView>(R.id.simple_listview);
//            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
//            listView.adapter = adapter
//            listView.onItemClickListener = object : AdapterView.OnItemClickListener {
//                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    Toast.makeText(applicationContext, "cliked" + list[position], Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

//        dialog.set

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }
    }

    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))

        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnected(p0: Bundle?) {

        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    fun searchLocation(view: View) {
        val locationSearch: EditText = findViewById(R.id.et_search)
        var location: String
        location = locationSearch.text.toString().trim()
        var addressList: List<Address>? = null

        print("yo")

        dialog.show()

        if (location == "") {
            Toast.makeText(this, "Entrez un lieu", Toast.LENGTH_SHORT).show()
        } else {
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 10)

//                val address = addressList!![0]
//                val latLng = LatLng(address.latitude, address.longitude)
//                mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
//                mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "oops", Toast.LENGTH_SHORT).show()

            }


        }
    }


    fun showTwoFever(view: View) {
        val latLng = LatLng(6.1378, 1.2125)
        mMap!!.addMarker(MarkerOptions().position(latLng).title("Hotel du 2 Fevrie"))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    fun showTwoOmo(view: View) {
        val latLng = LatLng(	6.1306535, 1.2445182)
        mMap!!.addMarker(MarkerOptions().position(latLng).title("ONOMO Hotel Lome"))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    fun showTwoNapo(view: View) {
        val latLng = LatLng(	6.137778, 1.212500)
        mMap!!.addMarker(MarkerOptions().position(latLng).title("Napoleon Lagune"))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    fun showTwoGolf(view: View) {
        val latLng = LatLng(		6.1235172, 1.223553)
        mMap!!.addMarker(MarkerOptions().position(latLng).title("Hotel du Golfe"))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

}