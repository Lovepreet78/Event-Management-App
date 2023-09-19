package com.example.eventmanagement.eventactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.eventmanagement.databinding.ActivityEventDisplayerBinding

import com.example.eventmanagement.eventmodel.EventModel
import com.example.eventmanagement.recyclerview.EventsRecyclerView
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class EventDisplayerActivity : AppCompatActivity() {


    lateinit var binding: ActivityEventDisplayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDisplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getEvents()

    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun getEvents() {

        GlobalScope.launch {

            val apiService = RetrofitClient.create()
            val call  = apiService.getEvents()
            call.enqueue(object :retrofit2.Callback<EventModel>{
                override fun onResponse(call: Call<EventModel>, response: Response<EventModel>) {
                    if (response.isSuccessful){
                        val responseList = response.body()!!
                        val eventList = responseList.content




                        val adapter = EventsRecyclerView(eventList,this@EventDisplayerActivity)



                        binding.eventRecyclerView.adapter=adapter
                        adapter.notifyDataSetChanged()

                        binding.eventRecyclerView.layoutManager = LinearLayoutManager(this@EventDisplayerActivity)

                        Toast.makeText(this@EventDisplayerActivity, "Done", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@EventDisplayerActivity, "not Done", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<EventModel>, t: Throwable) {
                    Toast.makeText(this@EventDisplayerActivity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                }

            })
//            call.enqueue(object : retrofit2.Callback<List<EventModel>>{
//                override fun onResponse(
//                    call: Call<List<EventModel>>,
//                    response: Response<List<EventModel>>
//                ) {
//                    if(response.isSuccessful){
//                        val responseList = response.body()!!
//                        val eventList = responseList[0].content
//
//
//
//                        val adapter = EventsRecyclerView(eventList,this@EventDisplayerActivity)
//
//
//
//                        binding.eventRecyclerView.adapter=adapter
//                        adapter.notifyDataSetChanged()
//
//                        binding.eventRecyclerView.layoutManager = LinearLayoutManager(this@EventDisplayerActivity)
//
//                        Toast.makeText(this@EventDisplayerActivity, "Done", Toast.LENGTH_SHORT).show()
//
//
//
//
//                    }
//                    else{
////                        binding.noEventsText.visibility = View.VISIBLE
//                        Toast.makeText(this@EventDisplayerActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//
//                override fun onFailure(call: Call<List<EventModel>>, t: Throwable) {
////                    binding.noEventsText.visibility = View.VISIBLE
//                    Toast.makeText(this@EventDisplayerActivity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
//                }
//
//            })
        }
    }
}