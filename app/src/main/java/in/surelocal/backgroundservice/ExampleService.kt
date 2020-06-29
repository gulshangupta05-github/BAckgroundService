package `in`.surelocal.backgroundservice

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context

import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new.view.*
import kotlin.math.log

private const val TAG = "ExampleService"

class ExampleService : Service() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var firestore: FirebaseFirestore

    //    private lateinit var locationCallback: LocationCallback
    lateinit var locationRequest: LocationRequest


    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate()
        locationRequest = LocationRequest.create()
        locationRequest.interval =4000
        locationRequest.fastestInterval = 2000
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        getLastLocation()
        checkSettingsAndStartLocationUpdates()


    }

    private fun checkSettingsAndStartLocationUpdates() {
        val request = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()
        val client = LocationServices.getSettingsClient(this)

        val locationSettingsResponseTask =
            client.checkLocationSettings(request)

        locationSettingsResponseTask.addOnSuccessListener {
            startLocanUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocanUpdates() {

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        val locationTask =
            fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener {

            if (it !=null ) {
                Log.d(TAG, "latitude${it.latitude}")
                Log.d(TAG, "longtitude${it.longitude}")
            } else {
                Log.d(TAG, "getLastLocation: Location was null")
            }
        }

    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult == null) {
                super.onLocationResult(locationResult)
            }
            for (location in locationResult!!.locations) {
//                Log.d(TAG, "onLocationResult: ${location.latitude}")
//                Log.d(TAG, "onLocationResult: ${location.longitude}")
                val latlong = "${location.latitude} ${location.longitude}"
                Log.d(TAG, "onLocationResult: $latlong")

                val data = UserInfo(
                    latlong = latlong
                )
                firestore = FirebaseFirestore.getInstance()
                firestore.collection("lattitude").document().set(data)
                    .addOnSuccessListener {
                        Toast.makeText(this@ExampleService, latlong, Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener() {
                        Toast.makeText(this@ExampleService, latlong, Toast.LENGTH_SHORT).show()
                    }

//                Log.d(TAG, "onLocationResult:$latlong${location.latitude} ${location.longitude}")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}

