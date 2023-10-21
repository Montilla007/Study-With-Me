package com.example.loginsignupswm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginsignupswm.databinding.ActivityInboxBinding

class InboxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInboxBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboxBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}