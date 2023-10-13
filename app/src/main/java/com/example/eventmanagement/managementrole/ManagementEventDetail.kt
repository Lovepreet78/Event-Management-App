package com.example.eventmanagement.managementrole

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eventmanagement.R
import com.example.eventmanagement.admin.eventManager.AdminEditEvent
import com.example.eventmanagement.databinding.ActivityManagementEventDetailBinding
import com.example.eventmanagement.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagementEventDetail : AppCompatActivity() {
    lateinit var binding: ActivityManagementEventDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentFromEvent = intent
        val title = intentFromEvent.getStringExtra("title")
        val content = intentFromEvent.getStringExtra("content")
        val startDay = intentFromEvent.getStringExtra("startDay")
        val endDay = intentFromEvent.getStringExtra("endDay")
        val location = intentFromEvent.getStringExtra("location")
        val id = intentFromEvent.getStringExtra("id")
        val startTime = intentFromEvent.getStringExtra("startTime")
        val endTime = intentFromEvent.getStringExtra("endTime")
        val passId = id?.toLong()

        binding.ManagementDeleteEvent.setOnClickListener {
            deleteEvent(passId)
        }
        binding.ManagementEditEvent.setOnClickListener {
            editEvent(passId,title,content,location,startDay,endDay,startTime,endTime)
        }
        binding.eventDetailTitle.text = title
        binding.eventDetailContent.text = content
        binding.eventDetailStart.text = startDay
        binding.eventDetailEnd.text = endDay
        binding.eventDetailLocation.text = location

    }

    private fun editEvent(
        passId: Long?,
        title: String?,
        content: String?,
        location: String?,
        startDay: String?,
        endDay: String?,
        startTime: String?,
        endTime: String?
    ) {
        val intentToEditEvent  = Intent(this@ManagementEventDetail, ManagementEditEvent::class.java)
        intentToEditEvent.putExtra("eventId",passId)
        intentToEditEvent.putExtra("title",title)
        intentToEditEvent.putExtra("content",content)
        intentToEditEvent.putExtra("location",location)
        intentToEditEvent.putExtra("startDay",startDay)
        intentToEditEvent.putExtra("endDay",endDay)
        intentToEditEvent.putExtra("startTime",startTime)
        intentToEditEvent.putExtra("endTime",endTime)
        startActivity(intentToEditEvent)
        finish()
    }



    private fun deleteEvent(passId: Long?) {
        val apiService = RetrofitClient.create()



        val call = passId?.let { it1 -> apiService.deleteManagementEvent(it1.toLong()) }
        if (call != null) {

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ManagementEventDetail, "Event Deleted Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ManagementEventDetail, "Not Deleted , Try Again", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ManagementEventDetail, "Not Deleted , Try Again!!", Toast.LENGTH_SHORT).show()
                }

            })
        }
        else{
            Toast.makeText(this@ManagementEventDetail, "Fail to delete!!", Toast.LENGTH_SHORT).show()
        }
    }
}