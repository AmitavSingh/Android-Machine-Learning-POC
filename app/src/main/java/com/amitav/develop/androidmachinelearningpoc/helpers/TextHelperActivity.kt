package com.amitav.develop.androidmachinelearningpoc.helpers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.amitav.develop.androidmachinelearningpoc.databinding.ActivityTextHelperBinding

open class TextHelperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextHelperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextHelperBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.btnPostComment.setOnClickListener {
            runClassifier()
        }
    }

   protected open fun runClassifier(input: String = binding.editTextTextPersonName.text.toString()) {

    }

    protected fun getOutputTextView() = binding.textView2
}