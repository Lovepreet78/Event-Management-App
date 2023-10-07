package com.example.eventmanagement.managementrole

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmanagement.R
import com.example.eventmanagement.databinding.ActivityManagementShowEventsBinding

class ManagementShowEvents : AppCompatActivity() {
    lateinit var binding: ActivityManagementShowEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementShowEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}