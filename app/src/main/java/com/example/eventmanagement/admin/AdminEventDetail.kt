package com.example.eventmanagement.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmanagement.R
import com.example.eventmanagement.databinding.ActivityAdminEventDetailBinding

class AdminEventDetail : AppCompatActivity() {
    private lateinit var binding: ActivityAdminEventDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentFromEvent = intent
        val title = intentFromEvent.getStringExtra("title")
        val content = intentFromEvent.getStringExtra("content")
        val startDay = intentFromEvent.getStringExtra("startDay")
        val endDay = intentFromEvent.getStringExtra("endDay")
        val location = intentFromEvent.getStringExtra("location")
        val id = intentFromEvent.getStringExtra("id")
        val postedAt = intentFromEvent.getStringExtra("postedAt")
        val postedBy = intentFromEvent.getStringExtra("postedBy")


        binding.eventDetailTitle.text = title
        binding.eventDetailContent.text = content
        binding.eventDetailStart.text = startDay
        binding.eventDetailEnd.text = endDay
        binding.eventDetailLocation.text = location
        binding.eventPostedAt.text = postedAt
        binding.eventPostedBy.text = postedBy
        binding.eventId.text = id
    }
}