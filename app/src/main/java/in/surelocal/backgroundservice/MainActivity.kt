package `in`.surelocal.backgroundservice

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btn_startService
import kotlinx.android.synthetic.main.activity_main.view.*

import kotlinx.android.synthetic.main.activity_new.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {


    var LOCATION_REQUEST_CODE = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_startService.setOnClickListener() {
            val serviceIntent = Intent(this, ExampleService::class.java)
//                .putExtra("inputExtra",editTextInput)
            ContextCompat.startForegroundService(this, serviceIntent)
            startService(serviceIntent)
//            stopService(serviceIntent)

            Log.d(TAG, "onCreate: start")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        val serviceIntent=Intent(this,ExampleService::class.java)
//        stopService(serviceIntent)
//        Log.d(TAG, "onDestroy: ${stopService(serviceIntent)}")
    }
    override fun onStart() {
        super.onStart()
        val permissionACL = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val permissionAFL = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionACL != PackageManager.PERMISSION_GRANTED || permissionAFL != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {

        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
            grantResults[1] != PackageManager.PERMISSION_GRANTED
        ) {
        }
    }
}