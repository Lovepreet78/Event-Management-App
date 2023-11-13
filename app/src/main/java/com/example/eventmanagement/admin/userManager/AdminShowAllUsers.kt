package com.example.eventmanagement.admin.userManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmanagement.R
import com.example.eventmanagement.admin.AdminEventsRecyclerview.AdminEventsViewAdapter
import com.example.eventmanagement.admin.userEventModel.AdminUserModel
import com.example.eventmanagement.admin.userManager.AdminUsersModel.AdminAllUsersModel
import com.example.eventmanagement.admin.userManager.AdminUsersModel.Content
import com.example.eventmanagement.admin.userManager.adminuserrecylerviewadapter.ShowUsersAdapter
import com.example.eventmanagement.databinding.ActivityAdminShowAllUsersBinding
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminShowAllUsers : AppCompatActivity() {
    lateinit var binding: ActivityAdminShowAllUsersBinding
    private var totalPages = 0;
    private val usersList = mutableListOf<Content>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminShowAllUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title ="All Users List"
        refreshLayout()
//        callForPageZero()



    }



    private  fun callForPageZero() {

            val apiService = RetrofitClient.create()
            val call  = apiService.getUsersForAdmin(0)
        call.enqueue(object : Callback<AdminAllUsersModel>{
            override fun onResponse(
                call: Call<AdminAllUsersModel>,
                response: Response<AdminAllUsersModel>
            ) {
                if(response.isSuccessful){
                    val users = response.body()
                    if (users != null) {
                        totalPages = users.totalPages
                    }
                    val toBeAddedToList  = users?.content
                    if (toBeAddedToList != null) {
                        usersList.addAll(toBeAddedToList)
                    }

                    setDataToAdapter()
                    callForNextPages()
                }
                else{
                    Toast.makeText(this@AdminShowAllUsers, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AdminAllUsersModel>, t: Throwable) {
                Toast.makeText(this@AdminShowAllUsers, "Failed to fetch users!!", Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun callForNextPages() {
        for(i in 1..totalPages){
            val apiService = RetrofitClient.create()
            val call  = apiService.getUsersForAdmin(i)

            call.enqueue(object : Callback<AdminAllUsersModel>{
                override fun onResponse(
                    call: Call<AdminAllUsersModel>,
                    response: Response<AdminAllUsersModel>
                ) {
                    if(response.isSuccessful){
                        val users = response.body()

                        val toBeAddedToList  = users?.content
                        if (toBeAddedToList != null) {
                            usersList.addAll(toBeAddedToList)
                        }
                        setDataToAdapter()

                    }
                    else{
                        Toast.makeText(this@AdminShowAllUsers, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AdminAllUsersModel>, t: Throwable) {
                    Toast.makeText(this@AdminShowAllUsers, "Failed to fetch users!!", Toast.LENGTH_SHORT).show()
                }

            })

        }



    }


    private fun setDataToAdapter() {


        val adapter = ShowUsersAdapter(usersList,this@AdminShowAllUsers)
        binding.adminUserRecyclerView.layoutManager = LinearLayoutManager(this@AdminShowAllUsers)
        binding.adminUserRecyclerView.adapter=adapter

        adapter.notifyDataSetChanged()

    }

    private  fun refreshLayout() {

        binding.swipeRefreshAdminModeUsers.setOnRefreshListener {
            usersList.clear()
            setDataToAdapter()
            runBlocking {
                val job = CoroutineScope(Dispatchers.IO).async {
                    callForPageZero()
                }
                job.await()
                binding.swipeRefreshAdminModeUsers.isRefreshing=false
            }
        }
    }

    override fun onResume() {
        usersList.clear()
        setDataToAdapter()
        super.onResume()
        callForPageZero()
    }


}