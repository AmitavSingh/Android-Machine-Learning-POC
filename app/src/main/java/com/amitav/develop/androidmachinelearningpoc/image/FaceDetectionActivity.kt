package com.amitav.develop.androidmachinelearningpoc.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.amitav.develop.androidmachinelearningpoc.Constants.TAG
import com.amitav.develop.androidmachinelearningpoc.helpers.BoxWithLabel
import com.amitav.develop.androidmachinelearningpoc.helpers.ImageHelperActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import java.io.IOException

class FaceDetectionActivity : ImageHelperActivity() {

    private lateinit var detector: FaceDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()


        detector = FaceDetection.getClient(options)

    }

    override fun runClassification(imageUri: Uri) {

        val bitmap = BitmapFactory.decodeStream(this.contentResolver.openInputStream(imageUri))
        val image = InputImage.fromBitmap(bitmap, 0)


        detector.process(image).addOnSuccessListener { faces ->

            getImageView().setImageBitmap(bitmap)
            if (faces.isEmpty()) {
                getTextView().text = "No Faces detected "
            } else {
                getTextView().text = "Face detected "
                val boxes = ArrayList<BoxWithLabel>()
                for (face in faces) {
                    boxes.add(
                        BoxWithLabel(
                            text = face.trackingId.toString(),
                            rect = face.boundingBox
                        )
                    )
                    drawDetectionResult(bitmap, boxes)

                }
            }
        }
            .addOnFailureListener {
                it.stackTrace
                Log.d(TAG, it.message.toString())
            }

    }
}