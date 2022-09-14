package com.amitav.develop.androidmachinelearningpoc

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amitav.develop.androidmachinelearningpoc.Constants.TAG
import com.amitav.develop.androidmachinelearningpoc.databinding.ActivityImageBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions


class ImageActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var binding: ActivityImageBinding
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var imageLabeler: ImageLabeler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        imageLabeler = ImageLabeling.getClient(
            ImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build()
        )


        if (!checkPermission()) {
            requestPermission()
        }

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.imageView.setImageURI(uri)
            if (uri != null) {
                runClassification(uri)
            }
        }

        binding.btnPickImage.setOnClickListener {
            imageChooser()
        }

        binding.btnStartCamera.setOnClickListener {

        }
    }

    private fun imageChooser() {
        getContent.launch("image/*")
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
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (storagePermission) {
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission Given...")
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission denied...")
                }
            }
        }
    }

    private fun runClassification(imageUri: Uri) {
        var inputImage = InputImage.fromFilePath(this, imageUri)
        imageLabeler.process(inputImage).addOnSuccessListener {
            if (it.size > 0) {
                var string = StringBuilder()
                for(imagelabel in it){
                    string.append(imagelabel.text)
                    string.append(" : ")
                    string.append(imagelabel.confidence)
                    string.append("\n")
                }
                binding.textView.text = string.toString()
            }

        }
            .addOnFailureListener {
                it.stackTrace
                Log.d(TAG, it.toString())
            }

    }


}