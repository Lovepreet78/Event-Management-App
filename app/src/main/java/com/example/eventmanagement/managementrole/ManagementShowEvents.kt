package com.example.eventmanagement.managementrole

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmanagement.R
import com.example.eventmanagement.admin.AdminEventsRecyclerview.AdminEventsViewAdapter
import com.example.eventmanagement.admin.userEventModel.AdminUserModel
import com.example.eventmanagement.databinding.ActivityManagementShowEventsBinding
import com.example.eventmanagement.managementrole.adapter.ManagementEventAdapter
import com.example.eventmanagement.managementrole.managementeventmodel.ManagementEventDTO
import com.example.eventmanagement.managementrole.managementeventmodel.ManagementEventsModel
import com.example.eventmanagement.retrofit.RetrofitClient
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
//                    Log.d("adminEE2",allEvents.toString()+" totalpages = $totalPages")
                    setDataToAdapter()
                    callForNextPages()
                }
                else{
                    Toast.makeText(this@ManagementShowEvents, "Admin call for event failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ManagementEventsModel>, t: Throwable) {
                Toast.makeText(this@ManagementShowEvents, "Admin call for event failed!!!!!!", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@ManagementShowEvents, "Management call for event failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ManagementEventsModel>, t: Throwable) {
                    Toast.makeText(this@ManagementShowEvents, "Management call for event failed!!!!!!", Toast.LENGTH_SHORT).show()
                }

            })
        }


    }
}