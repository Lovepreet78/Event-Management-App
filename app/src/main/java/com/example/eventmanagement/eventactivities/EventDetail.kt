package com.example.eventmanagement.eventactivities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.eventmanagement.R
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
        val startTime = intentFromEvent.getStringExtra("startTime")
        val endDay = intentFromEvent.getStringExtra("endDay")
        val endTime = intentFromEvent.getStringExtra("endTime")
        val location = intentFromEvent.getStringExtra("location")
        val registrationLink = intentFromEvent.getStringExtra("registrationLink")
        val imageLink = intentFromEvent.getStringExtra("imageLink")

        binding.eventDetailTitle.text = title
        binding.eventDetailContent.text = content
        binding.eventDetailStart.text = "$startDay - $startTime"
        binding.eventDetailEnd.text = "$endDay - $endTime"
        binding.eventDetailLocation.text = location
//        Glide.with(this).load(imageLink).into(binding.imageView1);
        if(imageLink=="" || imageLink==null){
            binding.imageView1.setImageResource(R.drawable.event_detail_image)
        }
        else{
            Glide.with(this).load(imageLink).into(binding.imageView1);
        }

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

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No Browser Available", Toast.LENGTH_SHORT).show()
        }

    }
}