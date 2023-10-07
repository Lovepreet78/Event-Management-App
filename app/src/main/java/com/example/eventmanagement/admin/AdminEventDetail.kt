package com.example.eventmanagement.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eventmanagement.databinding.ActivityAdminEventDetailBinding
import com.example.eventmanagement.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val startTime = intentFromEvent.getStringExtra("startTime")
        val endTime = intentFromEvent.getStringExtra("endTime")
        val passId = id?.toLong()
        binding.adminDeleteEvent.setOnClickListener{

            deleteEvent(passId)

        }
        binding.adminEditEvent.setOnClickListener {
            editEvent(passId,title,content,location,startDay,endDay,startTime,endTime)
        }

        binding.eventDetailTitle.text = title
        binding.eventDetailContent.text = content
        binding.eventDetailStart.text = startDay
        binding.eventDetailEnd.text = endDay
        binding.eventDetailLocation.text = location
        binding.eventPostedAt.text = postedAt
        binding.eventPostedBy.text = postedBy
        binding.eventId.text = id


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
        val intentToEditEvent  = Intent(this@AdminEventDetail,AdminEditEvent::class.java)
        intentToEditEvent.putExtra("eventId",passId)
        intentToEditEvent.putExtra("title",title)
        intentToEditEvent.putExtra("content",content)
        intentToEditEvent.putExtra("location",location)
        intentToEditEvent.putExtra("startDay",startDay)
        intentToEditEvent.putExtra("endDay",endDay)
        intentToEditEvent.putExtra("startTime",startTime)
        intentToEditEvent.putExtra("endTime",endTime)
        startActivity(intentToEditEvent)
    }

//    private fun editEvent(passId: Long?) {
//        val intentToEditEvent  = Intent(this@AdminEventDetail,AdminEditEvent::class.java)
//        intentToEditEvent.putExtra("eventId",passId)
//        startActivity(intentToEditEvent)
//
//    }

    private fun deleteEvent(passId: Long?) {
        val apiService = RetrofitClient.create()



        val call = passId?.let { it1 -> apiService.deleteEventByAdmin(it1.toLong()) }
        if (call != null) {

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminEventDetail, "Event Deleted Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AdminEventDetail, "Not Deleted , Try Again", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                }

            })
        }
        else{
            Toast.makeText(this@AdminEventDetail, "Fail to delete!!", Toast.LENGTH_SHORT).show()
        }
    }
}