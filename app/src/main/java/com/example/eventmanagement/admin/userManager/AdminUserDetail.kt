package com.example.eventmanagement.admin.userManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.eventmanagement.R
import com.example.eventmanagement.databinding.ActivityAdminUserDetailBinding

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

        }
        binding.changeToAdmin.setOnClickListener {

        }
        binding.changeToAdmin.setOnClickListener {

        }
    }
}