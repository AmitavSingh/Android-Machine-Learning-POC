package com.amitav.develop.androidmachinelearningpoc.helpers

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.amitav.develop.androidmachinelearningpoc.databinding.ActivityAudioHelperBinding

open class AudioHelperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioHelperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioHelperBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.stopRecording.isEnabled = false

        binding.startRecording.setOnClickListener {
            startRecording()
        }

        binding.stopRecording.setOnClickListener {
            stopRecording()
        }
    }

    protected open fun startRecording() {
        binding.startRecording.isEnabled = false
        binding.stopRecording.isEnabled = true
    }

    protected open fun stopRecording() {
        binding.startRecording.isEnabled = true
        binding.stopRecording.isEnabled = false
        binding.textOutput.text = ""
        binding.textSpecifications.text = ""
    }

    fun getSpecsTextView() = binding.textSpecifications

    fun getOutPutText() = binding.textOutput

}