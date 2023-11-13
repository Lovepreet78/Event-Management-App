package com.example.eventmanagement.eventactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmanagement.admin.eventManager.AdminShowAllEvents
import com.example.eventmanagement.admin.userManager.AdminShowAllUsers
import com.example.eventmanagement.constants.CurrentUserRole

import com.example.eventmanagement.databinding.ActivityEventDisplayerBinding
import com.example.eventmanagement.eventmodel.EventDTO

import com.example.eventmanagement.eventmodel.EventModel
import com.example.eventmanagement.eventactivities.recyclerview.EventsRecyclerView
import com.example.eventmanagement.managementrole.ManagementShowEvents
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Response

class EventDisplayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEventDisplayerBinding
    var allEvents = mutableListOf<EventDTO>()

    var totalPages : Int = 0;

    override  fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEventDisplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title ="Events"

        refreshLayout()




        binding.createNewEvents.setOnClickListener {
            if(binding.managementPanel.visibility==View.GONE && binding.AdminPanel.visibility==View.GONE){
                binding.managementPanel.visibility=View.VISIBLE
                binding.AdminPanel.visibility=View.VISIBLE
            }
            else{
                binding.managementPanel.visibility=View.GONE
                binding.AdminPanel.visibility=View.GONE
            }
        }
        binding.managementPanel.setOnClickListener {
            if(CurrentUserRole.currentUserRole=="[MANAGEMENT]" || CurrentUserRole.currentUserRole=="[ADMIN]") {
                val intentToManagement =
                    Intent(this@EventDisplayerActivity, ManagementShowEvents::class.java)
                startActivity(intentToManagement)
            }
            else{
                Toast.makeText(this@EventDisplayerActivity, "You are not eligible for this", Toast.LENGTH_SHORT).show()
            }
        }
        binding.AdminPanel.setOnClickListener {
            if(CurrentUserRole.currentUserRole=="[ADMIN]") {
                val intentToAdmin =
                    Intent(this@EventDisplayerActivity, AdminShowAllEvents::class.java)
                startActivity(intentToAdmin)
            }
            else{
                Toast.makeText(this@EventDisplayerActivity, "You are not eligible for this", Toast.LENGTH_SHORT).show()
            }
        }

//        GlobalScope.launch { getEvents() }









    }
    override fun onResume() {
        super.onResume()

        allEvents.clear()
        setDataTpoAdapter()

            getEvents()

        }

    private  fun refreshLayout() {
        binding.swipeRefreshGuestMode.setOnRefreshListener {
            allEvents.clear()
            setDataTpoAdapter()

            runBlocking {
                val job = CoroutineScope(Dispatchers.IO).async {
                    getEvents()
                }
                job.await()
                binding.swipeRefreshGuestMode.isRefreshing=false
            }
        }
    }

    private  fun getNextPagesEvents() {
        val apiService = RetrofitClient.create()
         GlobalScope.launch  {


            for (i in 1..totalPages) {


                val callForPages = apiService.getEventsPages(i)

                callForPages.enqueue(object : retrofit2.Callback<EventModel> {
                    override fun onResponse(
                        call: Call<EventModel>,
                        response: Response<EventModel>
                    ) {
                        if (response.isSuccessful) {
                            val responseList = response.body()!!
                            allEvents.addAll(responseList.content)
                            setDataTpoAdapter()




                        }
//                        else {
//                            Toast.makeText(
//                                this@EventDisplayerActivity,
//                                "A not Done",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
                    }

                    override fun onFailure(call: Call<EventModel>, t: Throwable) {
                        Toast.makeText(
                            this@EventDisplayerActivity,
                            "Something went wrong!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })


            }
        }

    }

    private fun setDataTpoAdapter() {
        val adapter = EventsRecyclerView(allEvents,this@EventDisplayerActivity)
        binding.eventRecyclerView.layoutManager = LinearLayoutManager(this@EventDisplayerActivity)
        binding.eventRecyclerView.adapter=adapter
        adapter.notifyDataSetChanged()
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

                        allEvents.addAll(eventList)

                        totalPages = responseList.totalPages


                        setDataTpoAdapter()
                        getNextPagesEvents()
                    }
                    else{
                        Toast.makeText(this@EventDisplayerActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<EventModel>, t: Throwable) {
                    Toast.makeText(this@EventDisplayerActivity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                }
            })

        }



    }
}