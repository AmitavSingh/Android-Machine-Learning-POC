package com.amitav.develop.androidmachinelearningpoc

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amitav.develop.androidmachinelearningpoc.audio.AudioClassificationActivity
import com.amitav.develop.androidmachinelearningpoc.audio.BirdIdentifierActivity
import com.amitav.develop.androidmachinelearningpoc.databinding.ActivityMainBinding
import com.amitav.develop.androidmachinelearningpoc.helpers.AudioHelperActivity
import com.amitav.develop.androidmachinelearningpoc.helpers.TextHelperActivity
import com.amitav.develop.androidmachinelearningpoc.image.FaceDetectionActivity
import com.amitav.develop.androidmachinelearningpoc.image.FlowerClassificationActivity
import com.amitav.develop.androidmachinelearningpoc.image.ImageClassificationActivity
import com.amitav.develop.androidmachinelearningpoc.image.ObjectDetectionActivity
import com.amitav.develop.androidmachinelearningpoc.text.SpamDetectionActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val PERMISSION_REQUEST_CODE = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        if (!checkPermission()) {
            requestPermission()
        }

        binding.button.setOnClickListener {
            startActivity(Intent(this, ImageClassificationActivity::class.java))
        }

        binding.button2.setOnClickListener {
            startActivity(Intent(this, FlowerClassificationActivity::class.java))
        }

        binding.button3.setOnClickListener {
            startActivity(Intent(this, ObjectDetectionActivity::class.java))
        }

        binding.button4.setOnClickListener {
            startActivity(Intent(this, FaceDetectionActivity::class.java))
        }

        binding.button5.setOnClickListener {
            startActivity(Intent(this, AudioClassificationActivity::class.java))
        }

        binding.btnBirdIdentification.setOnClickListener {
            startActivity(Intent(this, BirdIdentifierActivity::class.java))
        }

        binding.btnTextHelper.setOnClickListener {
            startActivity(Intent(this, SpamDetectionActivity::class.java))
        }
    }

    private fun checkPermission(): Boolean {
        val readStorage = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val camera = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val recordAudio = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        return readStorage && camera && recordAudio

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ),
            PERMISSION_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val readStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val audioPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED
                if (readStorage && cameraPermission && audioPermission) {
                    Log.d(
                        Constants.TAG,
                        "onRequestPermissionsResult:  Permission Given..."
                    )
                } else {
                    Log.d(
                        Constants.TAG,
                        "onRequestPermissionsResult: denied..."
                    )
                }
            }
        }
    }
}