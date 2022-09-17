package com.amitav.develop.androidmachinelearningpoc.audio

import android.media.AudioRecord
import android.os.Bundle
import com.amitav.develop.androidmachinelearningpoc.helpers.AudioHelperActivity
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import org.tensorflow.lite.task.audio.classifier.Classifications
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate


class BirdIdentifierActivity : AudioHelperActivity() {

    val model: String = "my_birds_model.tflite"
    lateinit var audioRecord: AudioRecord
    lateinit var timerTask: TimerTask
    lateinit var audioClassifier: AudioClassifier
    lateinit var tensorAudio: TensorAudio


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        audioClassifier = AudioClassifier.createFromFile(this, model)
        tensorAudio = audioClassifier.createInputTensorAudio()
    }

    override fun startRecording() {
        super.startRecording()

        val format = audioClassifier.requiredTensorAudioFormat
        val recorderSpecs = "Number Of Channels: ${format.channels}\n" +
                "Sample Rate: ${format.sampleRate}"
        getSpecsTextView().text = recorderSpecs

        audioRecord = audioClassifier.createAudioRecord()
        audioRecord.startRecording()

        timerTask = Timer().scheduleAtFixedRate(1, 500) {

            tensorAudio.load(audioRecord)
            val output: List<Classifications> = audioClassifier.classify(tensorAudio)
            var finalOutput: MutableList<Category> = ArrayList()
            for (category in output[0].categories) {
                if (category.score > 0.3f && category.label.equals("Bird")) {
                    finalOutput.add(category)
                }
            }

            if (finalOutput.isEmpty()) {
                return@scheduleAtFixedRate
            }

            finalOutput = ArrayList()
            for (category in output[1].categories) {
                if (category.score > 0.3f) {
                    finalOutput.add(category)
                }
            }


            val outputStr = StringBuilder()
            for (category in finalOutput) {
                outputStr.append(category.label)
                    .append(": ").append(category.score).append("\n")
            }

            runOnUiThread {
                if (finalOutput.isEmpty()) {
                    getOutPutText().text = "Could not classify"
                } else {
                    getOutPutText().text = outputStr.toString()
                }
            }
        }

    }

    override fun stopRecording() {
        super.stopRecording()
        timerTask.cancel()
        audioRecord.stop()
    }
}