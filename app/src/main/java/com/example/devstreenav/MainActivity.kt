package com.example.devstreenav


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.devstreenav.Activity.StoreLocationListActivity
import com.example.devstreenav.RoomDb.Mock
import com.example.devstreenav.RoomDb.Mock_DataBase
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : FragmentActivity(), OnMapReadyCallback {


    // below are the latitude and longitude
    // of 4 different locations.
    private lateinit var mMap: GoogleMap
    lateinit var mockList: List<Mock>
    var typr = ""
    var rajkot = LatLng(22.3038945, 70.80215989999999)
    var TamWorth = LatLng(23.022505, 72.5713621)
    var bhavnag = LatLng(21.7644725, 72.15193040000001)
    var jamanagr = LatLng(22.4707019, 70.05773)
    private var locationArrayList: ArrayList<LatLng> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        typr = intent.getStringExtra("type").toString()



        locationArrayList = ArrayList()

        // on below line we are adding our
        // locations in our array list.

        // on below line we are adding our
        // locations in our array list.


        // Initializing the Places API
        // with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyBSNyp6GQnnKlrMr7hD2HGiyF365tFlK5U")
        }


        val autocompleteSupportFragment1 =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment1) as AutocompleteSupportFragment?

        autocompleteSupportFragment1!!.setPlaceFields(
            listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.PHONE_NUMBER,
                Place.Field.LAT_LNG,
                Place.Field.OPENING_HOURS,
                Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL

            )
        )


        // Display the fetched information after clicking on one of the options
        autocompleteSupportFragment1.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                // Text view where we will
                // append the information that we fetch
//                val textView = findViewById<TextView>(R.id.tv1)

                // Information about the place
                val name = place.name
                val address = place.address
                val phone = place.phoneNumber.toString()
                val latlng = place.latLng
                findViewById<TextView>(R.id.saveMap).visibility = View.VISIBLE
                findViewById<TextView>(R.id.saveMap).setOnClickListener {

                    val db: Mock_DataBase = Mock_DataBase.getDbInstance(applicationContext)
                    val fav = Mock()
                    fav.name = name
                    fav.lat = place.latLng!!.latitude.toString()
                    fav.longi = place.latLng!!.longitude.toString()
                    db.mock_dao().inserImage(fav)



                    startActivity(Intent(this@MainActivity, StoreLocationListActivity::class.java))
                }
                locationArrayList.add(latlng!!)
                for (i in locationArrayList.indices) {
                    // below line is use to add marker to each location of our array list.
                    mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("Marker"))

                    // below line is use to zoom our camera on map.
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))

                    // below line is use to move our camera to the specific location.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList[i], 10F))
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList!![i]),)
                }
                Log.d("TAG", "onCreate: " + latlng)
                Log.d("TAG", "onCreate: " + name)

            }

            override fun onError(status: Status) {
                Toast.makeText(applicationContext, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDirectionsUrl(markerPoints: ArrayList<LatLng>): List<String>? {
        for (i in markerPoints.indices) {
            // below line is use to add marker to each location of our array list.
            mMap.addMarker(MarkerOptions().position(markerPoints[i]).title("Marker"))

            // below line is use to zoom our camera on map.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))

            // below line is use to move our camera to the specific location.
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPoints[i], 10F))
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList!![i]),)
        }
        val mUrls: MutableList<String> = ArrayList()
        if (markerPoints.size > 1) {
            var str_origin = markerPoints[0].latitude.toString() + "," + markerPoints[0].longitude
            var str_dest = markerPoints[1].latitude.toString() + "," + markerPoints[1].longitude
            val sensor = "sensor=false"
            var parameters = "origin=$str_origin&destination=$str_dest&$sensor"
            val output = "json"
            var url =
                "https://maps.googleapis.com/maps/api/directions/$output?$parameters"+ "&sensor=false" +
                        "&mode=driving" +
                        "&key=AIzaSyBSNyp6GQnnKlrMr7hD2HGiyF365tFlK5U"
            GetDirection(url).execute()
            for (i in 2 until markerPoints.size)  //loop starts from 2 because 0 and 1 are already printed
            {
                str_origin = str_dest
                str_dest = markerPoints[i].latitude.toString() + "," + markerPoints[i].longitude
                parameters = "origin=$str_origin&destination=$str_dest&$sensor"
                url =
                    "https://maps.googleapis.com/maps/api/directions/$output?$parameters" + "&sensor=false" +
                            "&mode=driving" +
                            "&key=AIzaSyBSNyp6GQnnKlrMr7hD2HGiyF365tFlK5U"
                GetDirection(url).execute()
            }
        }
        return mUrls
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url: String) :
        AsyncTask<Void, Void, List<List<LatLng>>>() {
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices) {
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.GREEN)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap;
//        locationArrayList!!.add(rajkot)
//        locationArrayList!!.add(TamWorth)
//        locationArrayList!!.add(bhavnag)
//        locationArrayList!!.add(jamanagr)
        val db: Mock_DataBase = Mock_DataBase.getDbInstance(applicationContext)
        mockList = db.mock_dao().getAllMockList()
        if (typr == "route") {
            for (i in 0 until mockList.size) {
                locationArrayList.add(
                    LatLng(
                        mockList[i].lat.toDouble(),
                        mockList[i].longi.toDouble()
                    )
                )
            }
            Log.d("TAG", "onCreate: " + locationArrayList.size)
            getDirectionsUrl(locationArrayList)
        }else if(typr == "pin"){
            setPin()
        }
//        getDirectionsUrl(locationArrayList)
    }
    fun setPin(){
        for (i in 0 until mockList.size){
//            var id = intent.getStringExtra("id")!!.toInt()
//            Log.d("TAG", "setPin: "+id.toString())
            if(mockList[i].id == intent.getIntExtra("id",0)){
                mMap.addMarker(MarkerOptions().position(LatLng(mockList[i].lat.toDouble(),mockList[i].longi.toDouble())).title(mockList[i].name.toString()))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mockList[i].lat.toDouble(),mockList[i].longi.toDouble()), 10F))
            }
        }
    }
}

class MapData {
    var routes = ArrayList<Routes>()
}

class Routes {
    var legs = ArrayList<Legs>()
}

class Legs {
    var distance = Distance()
    var duration = Duration()
    var end_address = ""
    var start_address = ""
    var end_location = Location()
    var start_location = Location()
    var steps = ArrayList<Steps>()
}

class Steps {
    var distance = Distance()
    var duration = Duration()
    var end_address = ""
    var start_address = ""
    var end_location = Location()
    var start_location = Location()
    var polyline = PolyLine()
    var travel_mode = ""
    var maneuver = ""
}

class Duration {
    var text = ""
    var value = 0
}

class Distance {
    var text = ""
    var value = 0
}

class PolyLine {
    var points = ""
}

class Location {
    var lat = ""
    var lng = ""
}