package competer73android.httpsgithub.pockemon

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermisson()
        loadPockemons()
    }

    var ACCESSLOCATION = 123
    fun checkPermisson() {
        if(Build.VERSION.SDK_INT >= 23) {
            if(ActivityCompat.checkSelfPermission
                              (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
               != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                                   ACCESSLOCATION)
                return
            }
        }
        getUserLocation()
    }

    fun getUserLocation() {
        Toast.makeText(this, "User location access on", Toast.LENGTH_LONG).show()

        var myLocation      = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // This is not an error permission was checked before calling gerUserLocation.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                               3,
                                               3f,
                                               myLocation)
        var myThread = MyThread()
        myThread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when(requestCode)
        {
            ACCESSLOCATION->
            {
                if(grantResults[0]
                        ==PackageManager.PERMISSION_GRANTED)
                {
                    getUserLocation()
                }
                else
                {
                    Toast.makeText(this,
                                   "We cannot access to your location",
                                   Toast.LENGTH_LONG)
                            .show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    var location:Location?=null

    // Get user location
    inner class MyLocationListener:LocationListener {
        constructor()
        {
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.longitude = 0.0
        }

        override fun onLocationChanged(p0: Location?) {
            location=p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    var oldLocation:Location?=null
    inner class MyThread:Thread
    {
        constructor():super()
        {
            oldLocation=Location("Start")
            oldLocation!!.latitude=0.0
            oldLocation!!.longitude=0.0
        }
        override fun run()
        {
            var zoom = true
            while (true)
            {
                try
                {
                    // Do not try if location does not changed!
                    if(oldLocation!!.distanceTo(location)==0f) continue
                    oldLocation=location

                    runOnUiThread {
                        mMap!!.clear()
                        // Add a marker in and move the camera
                        val myLoc = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                                .position(myLoc)
                                .title("Me")
                                .snippet(" here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                        )

                        // Make a zoom of camera only once
                        if (zoom) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 10f))
                            zoom = false
                        } else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc))
                        }



                        // Show pockemons
                        for (p in 0 until listPockemons.size) {
                            var pockemon = listPockemons[p]
                            if (pockemon.isCatch == false) {
                                val pockemonLoc = LatLng(pockemon.location!!.latitude,
                                                         pockemon.location!!.longitude )
                                if (location!!.distanceTo(pockemon.location)<2)
                                {
                                    pockemon.isCatch=true
                                    playerPower+=pockemon.power!!
                                    Toast.makeText(applicationContext,
                                            "You catch new pockemon your new power is "+playerPower,
                                            Toast.LENGTH_LONG).show()
                                }
                                if (pockemon.isCatch==false) mMap!!.addMarker(MarkerOptions()
                                                    .position(pockemonLoc)
                                                    .title(pockemon.name)
                                                    .snippet(pockemon.des)
                                                    .icon(BitmapDescriptorFactory
                                                            .fromResource(pockemon.image!!)
                                                    )
                                )
                            }
                        }
                    }

                    Thread.sleep(1000)
                }
                catch (ex:Exception) {}
            }
        }
    }

    // Load pockemons and define player power
    var playerPower=0.0
    var listPockemons=ArrayList<Pockemon>()

    fun loadPockemons()
    {
        listPockemons.add(
                Pockemon(
                        R.drawable.charmander,
                        "Charmander",
                        "Charmander living in japan",
                        55.0,
                        36.510589,
                        140.159157
                )
        )
        listPockemons.add(
                Pockemon(
                        R.drawable.bulbasaur,
                        "Bulbasaur",
                        "Bulbasaur living in usa",
                        90.5,
                        44.309248,
                        -103.098469
                )
        )
        listPockemons.add(
                Pockemon(
                        R.drawable.squirtle,
                        "Squirtle",
                        "Squirtle living in iraq",
                        33.5,
                        32.998502,
                        45.886656
                )
        )
    }
}























