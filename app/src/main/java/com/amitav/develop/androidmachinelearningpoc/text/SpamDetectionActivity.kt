package com.amitav.develop.androidmachinelearningpoc.text

import android.os.Bundle
import com.amitav.develop.androidmachinelearningpoc.helpers.TextHelperActivity
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier

class SpamDetectionActivity : TextHelperActivity() {

    val model_path: String = "model_spam.tflite"
    lateinit var classifier: NLClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        classifier = NLClassifier.createFromFile(this, model_path)
    }

    override fun runClassifier(input: String) {
        val apiResult = classifier.classify(input)
        val score = apiResult[1].score
        if (score > 0.8f){
            getOutputTextView().text = "Spam Detected with Score - $score"
        }else
            getOutputTextView().text = "No Spam Detected with Score - $score"

    }

}