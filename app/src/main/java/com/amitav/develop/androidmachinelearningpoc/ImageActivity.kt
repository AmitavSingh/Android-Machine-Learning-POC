package com.amitav.develop.androidmachinelearningpoc

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amitav.develop.androidmachinelearningpoc.Constants.TAG


class ImageActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        if (!checkPermission()) {
            requestPermission()
        }


    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.isNotEmpty()){
                val storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (storagePermission){
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission Given...")
                }
                else{
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission denied...")
                }
            }
        }
    }





}