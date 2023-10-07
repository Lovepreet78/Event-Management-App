package com.example.eventmanagement.admin.userManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.eventmanagement.R
import com.example.eventmanagement.databinding.ActivityAdminUserDetailBinding
import com.example.eventmanagement.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminUserDetail : AppCompatActivity() {
    lateinit var binding: ActivityAdminUserDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentFromUserClick = intent
        val userId = intentFromUserClick.getLongExtra("userId",0)
        val userName = intentFromUserClick.getStringExtra("userName")
        val userRole = intentFromUserClick.getStringExtra("userRole")
        binding.userId.text = userId.toString()
        binding.userName.text = userName
        binding.userRole.text = userRole

        binding.changeRole.setOnClickListener {
            binding.changeRolesOption.visibility = View.VISIBLE

        }
        binding.changeToAdmin.setOnClickListener {
            binding.changeRolesOption.visibility = View.VISIBLE
            updateRole(userId,"ADMIN")
        }
        binding.changeToAdmin.setOnClickListener {
            binding.changeRolesOption.visibility = View.VISIBLE
            updateRole(userId,"MANAGEMENT")
        }
        binding.changeToAdmin.setOnClickListener {
            binding.changeRolesOption.visibility = View.VISIBLE
            updateRole(userId,"USER")
        }
    }

    private fun updateRole(id:Long,role: String) {
        val apiService = RetrofitClient.create()
        val call = apiService.updateUserRole(id,role)

        call.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Toast.makeText(this@AdminUserDetail, "Role Changed to $role Successfully", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@AdminUserDetail, "Failed to change Role", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AdminUserDetail, "Failed to change Role!!", Toast.LENGTH_SHORT).show()
            }

        })

    }
}