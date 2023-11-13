package com.example.eventmanagement.eventactivities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.eventmanagement.databinding.ActivityEventDetailBinding

class EventDetail : AppCompatActivity() {
    private lateinit var binding: ActivityEventDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intentFromEvent = intent
        val title = intentFromEvent.getStringExtra("title")
        val content = intentFromEvent.getStringExtra("content")
        val startDay = intentFromEvent.getStringExtra("startDay")
        val endDay = intentFromEvent.getStringExtra("endDay")
        val location = intentFromEvent.getStringExtra("location")
        val registrationLink = intentFromEvent.getStringExtra("registrationLink")
        val imageLink = intentFromEvent.getStringExtra("imageLink")

        binding.eventDetailTitle.text = title
        binding.eventDetailContent.text = content
        binding.eventDetailStart.text = startDay
        binding.eventDetailEnd.text = endDay
        binding.eventDetailLocation.text = location
        Glide.with(this).load(imageLink).into(binding.imageView1);

        binding.registerToEventButton.setOnClickListener {
            if(registrationLink!=null) {
                intentToBrowser(registrationLink!!)
            }
            else
                Toast.makeText(this, "No Registration", Toast.LENGTH_SHORT).show()
        }



    }

    private fun intentToBrowser(regLink: String) {
        if(regLink=="") return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(regLink))

        // Check if there is a web browser available to handle the intent
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No Browser Available", Toast.LENGTH_SHORT).show()
        }

    }
}