package com.amitav.develop.androidmachinelearningpoc.helpers

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.amitav.develop.androidmachinelearningpoc.BuildConfig
import com.amitav.develop.androidmachinelearningpoc.databinding.ActivityImageBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


open class ImageHelperActivity : AppCompatActivity() {


    private lateinit var binding: ActivityImageBinding
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private var cameraImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        init()


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

    open fun runClassification(imageUri: Uri) {

    }

    open fun getTextView() = binding.textView

    open fun getImageView() = binding.imageView

    fun drawDetectionResult(
        bitmap: Bitmap,
        detectionResults: List<BoxWithLabel>
    ){
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val pen = Paint()
        pen.textAlign = Paint.Align.LEFT
        for (box in detectionResults) {
            // draw bounding box
            pen.color = Color.RED
            pen.strokeWidth = 8f
            pen.style = Paint.Style.STROKE
            canvas.drawRect(box.rect, pen)
            val tagSize = Rect(0, 0, 0, 0)

            // calculate the right font size
            pen.style = Paint.Style.FILL_AND_STROKE
            pen.color = Color.YELLOW
            pen.strokeWidth = 2f
            pen.textSize = 96f
            pen.getTextBounds(box.text, 0, box.text.length, tagSize)
            val fontSize: Float = pen.textSize * box.rect.width() / tagSize.width()

            // adjust the font size so texts are inside the bounding box
            if (fontSize < pen.textSize) {
                pen.textSize = fontSize
            }
            var margin: Float = (box.rect.width() - tagSize.width()) / 2.0f
            if (margin < 0f) margin = 0f
            canvas.drawText(
                box.text, box.rect.left + margin,
                (box.rect.top + tagSize.height()).toFloat(), pen
            )
        }

        binding.imageView.setImageBitmap(outputBitmap)
    }


}