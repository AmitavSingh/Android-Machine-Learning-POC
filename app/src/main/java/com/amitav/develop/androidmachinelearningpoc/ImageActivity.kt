package com.amitav.develop.androidmachinelearningpoc

import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.amitav.develop.androidmachinelearningpoc.Constants.TAG
import com.amitav.develop.androidmachinelearningpoc.databinding.ActivityImageBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ImageActivity : AppCompatActivity() {


    private lateinit var binding: ActivityImageBinding
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var imageLabeler: ImageLabeler
    private var cameraImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        init()

        imageLabeler = ImageLabeling.getClient(
            ImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build()
        )
    }

    private fun init() {

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                runClassification(uri)
            }
        }


        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess && cameraImageUri != null) {
                    runClassification(cameraImageUri!!)
                } else {
                    Toast.makeText(this, "Failed to capture Image", Toast.LENGTH_SHORT).show()
                }
            }



        binding.btnPickImage.setOnClickListener {
            imageChooser()
        }

        binding.btnStartCamera.setOnClickListener {
            openCamera()
        }
    }

    private fun imageChooser() {
        getContent.launch("image/*")
    }


    private fun openCamera() {
        val timeStamp = SimpleDateFormat.getDateTimeInstance().format(Date())
        var fileDirectory = File(getExternalFilesDir(DIRECTORY_PICTURES), "ML_IMAGES")
        if (!fileDirectory.exists()) {
            fileDirectory.mkdir()
        }
        val photoFile = File(
            fileDirectory.path,
            "JPEG_${timeStamp}_.jpg"
        )

        photoFile.also {
            cameraImageUri = FileProvider.getUriForFile(
                applicationContext,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                it
            )
            takePictureLauncher.launch(cameraImageUri)
        }
    }

    private fun runClassification(imageUri: Uri) {
        binding.imageView.setImageURI(imageUri)
        Log.d(TAG, imageUri.path.toString())
        var inputImage = InputImage.fromFilePath(this, imageUri)
        imageLabeler.process(inputImage).addOnSuccessListener {
            if (it.size > 0) {
                var string = StringBuilder()
                for (imagelabel in it) {
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