package com.amitav.develop.androidmachinelearningpoc.image

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.amitav.develop.androidmachinelearningpoc.Constants.TAG
import com.amitav.develop.androidmachinelearningpoc.helpers.BoxWithLabel
import com.amitav.develop.androidmachinelearningpoc.helpers.ImageHelperActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions


class ObjectDetectionActivity : ImageHelperActivity() {

    private lateinit var objectDetector: ObjectDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        objectDetector = ObjectDetection.getClient(options)

    }


    override fun runClassification(imageUri: Uri) {

        getImageView().setImageURI(imageUri)
        val bitmap = BitmapFactory.decodeStream(this.contentResolver.openInputStream(imageUri))
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        objectDetector.process(inputImage).addOnSuccessListener {
            if (it.isNotEmpty()) {
                val string = StringBuilder()
                val boxes = ArrayList<BoxWithLabel>()
                for (detectedObjects in it) {
                    if (detectedObjects.labels.isNotEmpty()) {
                        val labels = detectedObjects.labels[0].text
                        string.append(labels)
                            .append(" : ")
                            .append(detectedObjects.labels[0].confidence)
                            .append("\n")
                        boxes.add(BoxWithLabel(labels, detectedObjects.boundingBox))
                    } else {
                        string.append("Unknown").append("\n")
                    }
                }
                getTextView().text = string.toString()
                drawDetectionResult(bitmap = bitmap, detectionResults = boxes)
            } else {
                getTextView().text = "Failed to detect"
            }
        }
            .addOnFailureListener {
                Log.d(TAG, it.toString())
            }
    }
}