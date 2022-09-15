package com.amitav.develop.androidmachinelearningpoc.image

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.amitav.develop.androidmachinelearningpoc.Constants
import com.amitav.develop.androidmachinelearningpoc.helpers.ImageHelperActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ImageClassificationActivity : ImageHelperActivity() {

    private lateinit var imageLabeler: ImageLabeler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLabeler = ImageLabeling.getClient(
            ImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build()
        )
    }

    override fun runClassification(imageUri: Uri) {
        getImageView().setImageURI(imageUri)
        Log.d(Constants.TAG, imageUri.path.toString())
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
                getTextView().text = string.toString()
            }

        }
            .addOnFailureListener {
                it.stackTrace
                Log.d(Constants.TAG, it.toString())
            }
    }







}