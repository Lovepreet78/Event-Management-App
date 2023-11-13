package com.example.eventmanagement.managementrole

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmanagement.databinding.ActivityManagementShowEventsBinding
import com.example.eventmanagement.eventactivities.EventPostData
import com.example.eventmanagement.managementrole.adapter.ManagementEventAdapter
import com.example.eventmanagement.managementrole.managementeventmodel.ManagementEventDTO
import com.example.eventmanagement.managementrole.managementeventmodel.ManagementEventsModel
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Response

class ManagementShowEvents : AppCompatActivity() {
     lateinit var binding: ActivityManagementShowEventsBinding
    var totalPages:Int=0
    var allEvents = mutableListOf<ManagementEventDTO>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityManagementShowEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        refreshLayout()
        supportActionBar?.title ="My Events : Management"
        binding.createNewEvents.setOnClickListener {
            val intentToPost = Intent(this@ManagementShowEvents, EventPostData::class.java)
            startActivity(intentToPost)
        }

//        callForPageZero()

    }
    override fun onResume() {
        allEvents.clear()
        setDataToAdapter()
        super.onResume()
//        binding = ActivityManagementShowEventsBinding.inflate(layoutInflater)
        callForPageZero()
    }

    private  fun callForPageZero() {

        val apiService = RetrofitClient.create()
        val call  = apiService.getManagementEvents(0)
        call.enqueue(object : retrofit2.Callback<ManagementEventsModel>{
            override fun onResponse(
                call: Call<ManagementEventsModel>,
                response: Response<ManagementEventsModel>
            ) {
                if(response.isSuccessful){
                    val responseList = response.body()!!
                    val eventList = responseList.content


                    allEvents.addAll(eventList)



                    totalPages = responseList.totalPages

                    setDataToAdapter()
                    callForNextPages()
                }
                else{
                    Toast.makeText(this@ManagementShowEvents, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ManagementEventsModel>, t: Throwable) {
                Toast.makeText(this@ManagementShowEvents, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setDataToAdapter() {


        val adapter = ManagementEventAdapter(allEvents,this@ManagementShowEvents)
        binding.managementEventRecyclerView.layoutManager = LinearLayoutManager(this@ManagementShowEvents)
        binding.managementEventRecyclerView.adapter=adapter

        adapter.notifyDataSetChanged()

    }

    private fun callForNextPages() {

        for (i in 1..totalPages){
            val apiService = RetrofitClient.create()
            val call  = apiService.getManagementEvents(i)
            call.enqueue(object : retrofit2.Callback<ManagementEventsModel>{
                override fun onResponse(
                    call: Call<ManagementEventsModel>,
                    response: Response<ManagementEventsModel>
                ) {
                    if(response.isSuccessful){
                        val responseList = response.body()!!
                        val eventList = responseList.content
                        allEvents.addAll(eventList)


                        setDataToAdapter()
                    }
                    else{
                        Toast.makeText(this@ManagementShowEvents, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ManagementEventsModel>, t: Throwable) {
                    Toast.makeText(this@ManagementShowEvents, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
                }

            })
        }


    }

    private  fun refreshLayout() {
//        binding = ActivityManagementShowEventsBinding.inflate(layoutInflater)

        binding.swipeRefreshManagementModeEvents.setOnRefreshListener {
            allEvents.clear()
            setDataToAdapter()

            runBlocking {
                val job = CoroutineScope(Dispatchers.IO).async {
                    callForPageZero()
                }
                job.await()
                binding.swipeRefreshManagementModeEvents.isRefreshing=false
            }
        }
    }

}