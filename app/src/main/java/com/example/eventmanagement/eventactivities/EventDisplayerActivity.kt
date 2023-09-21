package com.example.eventmanagement.eventactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.eventmanagement.databinding.ActivityEventDisplayerBinding
import com.example.eventmanagement.eventmodel.EventDTO

import com.example.eventmanagement.eventmodel.EventModel
import com.example.eventmanagement.eventactivities.recyclerview.EventsRecyclerView
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.awt.font.NumericShaper.Range

class EventDisplayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEventDisplayerBinding
    var allEvents = mutableListOf<EventDTO>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDisplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        GlobalScope.launch {
            getEvents()
//        }

        binding.createNewEvents.setOnClickListener {

            val intentToCreateNewEvent = Intent(this@EventDisplayerActivity,EventPostData::class.java)
            startActivity(intentToCreateNewEvent)
        }

    }
    @OptIn(DelicateCoroutinesApi::class)
    private  fun getEvents() {

        GlobalScope.launch {
            var totalPages = 0;


            val apiService = RetrofitClient.create()
            val call  = apiService.getEvents()
            call.enqueue(object :retrofit2.Callback<EventModel>{
                override fun onResponse(call: Call<EventModel>, response: Response<EventModel>) {
                    if (response.isSuccessful){
                        val responseList = response.body()!!
                        val eventList = responseList.content

                        allEvents.addAll(eventList)

                        totalPages = responseList.totalPages


//                        val adapter = EventsRecyclerView(eventList,this@EventDisplayerActivity)
//
//
//
//                        binding.eventRecyclerView.adapter=adapter
//                        adapter.notifyDataSetChanged()
//
//                        binding.eventRecyclerView.layoutManager = LinearLayoutManager(this@EventDisplayerActivity)

                        Toast.makeText(this@EventDisplayerActivity, "P Done", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@EventDisplayerActivity, "P not Done", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<EventModel>, t: Throwable) {
                    Toast.makeText(this@EventDisplayerActivity, "P Something went wrong!!", Toast.LENGTH_SHORT).show()
                }

            })


            for(i in 1..totalPages){
                val callForPages  = apiService.getEventsPages(i)

                callForPages.enqueue(object :retrofit2.Callback<EventModel>{
                    override fun onResponse(call: Call<EventModel>, response: Response<EventModel>) {
                        if (response.isSuccessful){
                            val responseList = response.body()!!
                            allEvents.addAll(responseList.content)


                            Toast.makeText(this@EventDisplayerActivity, "A Done", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@EventDisplayerActivity, "A not Done", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<EventModel>, t: Throwable) {
                        Toast.makeText(this@EventDisplayerActivity, "A Something went wrong!!", Toast.LENGTH_SHORT).show()
                    }

                })



            }

            val adapter = EventsRecyclerView(allEvents,this@EventDisplayerActivity)
            binding.eventRecyclerView.adapter=adapter
            adapter.notifyDataSetChanged()
            binding.eventRecyclerView.layoutManager = LinearLayoutManager(this@EventDisplayerActivity)

        }



    }
}