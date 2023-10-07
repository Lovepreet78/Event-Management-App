package com.example.eventmanagement.admin.eventManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmanagement.R
import com.example.eventmanagement.admin.AdminEventsRecyclerview.AdminEventsViewAdapter
import com.example.eventmanagement.admin.userEventModel.AdminUserDTO
import com.example.eventmanagement.admin.userEventModel.AdminUserModel
import com.example.eventmanagement.databinding.ActivityAdminShowAllEventsBinding
import com.example.eventmanagement.eventactivities.recyclerview.EventsRecyclerView
import com.example.eventmanagement.eventmodel.EventModel
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Response

class AdminShowAllEvents : AppCompatActivity() {
    private lateinit var binding: ActivityAdminShowAllEventsBinding
    var allEvents = mutableListOf<AdminUserDTO>()
    var totalPages:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminShowAllEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)



            Toast.makeText(this@AdminShowAllEvents, "in runblocking", Toast.LENGTH_SHORT).show()

            callForPageZero()
            Log.d("adminqq","tot = $totalPages")


    }

    private fun setDataToAdapter() {


        val adapter = AdminEventsViewAdapter(allEvents,this@AdminShowAllEvents)
        binding.eventsAdminRecyclerView.layoutManager = LinearLayoutManager(this@AdminShowAllEvents)
        binding.eventsAdminRecyclerView.adapter=adapter

        adapter.notifyDataSetChanged()

    }

    private fun callForNextPages() {

            for (i in 1..totalPages){
                val apiService = RetrofitClient.create()
                val call  = apiService.getAllEventsForAdmin(i)
                call.enqueue(object : retrofit2.Callback<AdminUserModel>{
                    override fun onResponse(
                        call: Call<AdminUserModel>,
                        response: Response<AdminUserModel>
                    ) {
                        if(response.isSuccessful){
                            val responseList = response.body()!!
                            val eventList = responseList.content
                            allEvents.addAll(eventList)


                            setDataToAdapter()
                        }
                        else{
                            Toast.makeText(this@AdminShowAllEvents, "Admin call for event failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<AdminUserModel>, t: Throwable) {
                        Toast.makeText(this@AdminShowAllEvents, "Admin call for event failed!!!!!!", Toast.LENGTH_SHORT).show()
                    }

                })
            }


    }

    private  fun callForPageZero() {

            val apiService = RetrofitClient.create()
            val call  = apiService.getAllEventsForAdmin(0)
            call.enqueue(object : retrofit2.Callback<AdminUserModel>{
                override fun onResponse(
                    call: Call<AdminUserModel>,
                    response: Response<AdminUserModel>
                ) {
                    if(response.isSuccessful){
                        val responseList = response.body()!!
                        val eventList = responseList.content


                        allEvents.addAll(eventList)



                        totalPages = responseList.totalPages
                        Log.d("adminEE2",allEvents.toString()+" totalpages = $totalPages")
                        setDataToAdapter()
                        callForNextPages()
                    }
                    else{
                        Toast.makeText(this@AdminShowAllEvents, "Admin call for event failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AdminUserModel>, t: Throwable) {
                    Toast.makeText(this@AdminShowAllEvents, "Admin call for event failed!!!!!!", Toast.LENGTH_SHORT).show()
                }

            })
        }












}