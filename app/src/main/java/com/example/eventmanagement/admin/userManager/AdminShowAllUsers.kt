package com.example.eventmanagement.admin.userManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmanagement.R
import com.example.eventmanagement.admin.AdminEventsRecyclerview.AdminEventsViewAdapter
import com.example.eventmanagement.admin.userEventModel.AdminUserModel
import com.example.eventmanagement.admin.userManager.AdminUsersModel.Content
import com.example.eventmanagement.admin.userManager.adminuserrecylerviewadapter.ShowUsersAdapter
import com.example.eventmanagement.databinding.ActivityAdminShowAllUsersBinding
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class AdminShowAllUsers : AppCompatActivity() {
    lateinit var binding: ActivityAdminShowAllUsersBinding
    private var totalPages = 0;
    private val usersList = mutableListOf<Content>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminShowAllUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callForPageZero()

    }



    private  fun callForPageZero() {
        GlobalScope.launch {
            val apiService = RetrofitClient.create()
            val call  = apiService.getUsersForAdmin(0)
            try {
                val response = call.execute()
                if(response.isSuccessful){
                    val users = response.body()
                    if (users != null) {
                        totalPages = users.totalPages
                    }
                    val toBeAddedToList  = users?.content
                    if (toBeAddedToList != null) {
                        usersList.addAll(toBeAddedToList)
                    }
                    callForNextPages()
                }
                else{
                    Toast.makeText(this@AdminShowAllUsers, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                }
            }
            catch (e:Exception){
                Toast.makeText(this@AdminShowAllUsers, "Failed to fetch users!!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun callForNextPages() {
        for(i in 1..totalPages){
            val apiService = RetrofitClient.create()
            val call  = apiService.getUsersForAdmin(i)
            try {
                val response = call.execute()
                if(response.isSuccessful){
                    val users = response.body()
//                    if (users != null) {
//                        totalPages = users.totalPages
//                    }
                    val toBeAddedToList  = users?.content
                    if (toBeAddedToList != null) {
                        usersList.addAll(toBeAddedToList)
                    }

                }
                else{
                    Toast.makeText(this@AdminShowAllUsers, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                }
            }
            catch (e:Exception){
                Toast.makeText(this@AdminShowAllUsers, "Failed to fetch users!!", Toast.LENGTH_SHORT).show()
            }
        }
        setDataToAdapter()

    }
    private fun setDataToAdapter() {


        val adapter = ShowUsersAdapter(usersList,this@AdminShowAllUsers)
        binding.adminUserRecyclerView.layoutManager = LinearLayoutManager(this@AdminShowAllUsers)
        binding.adminUserRecyclerView.adapter=adapter

        adapter.notifyDataSetChanged()

    }




}