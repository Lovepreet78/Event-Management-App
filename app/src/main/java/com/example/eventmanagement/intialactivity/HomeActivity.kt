package com.example.eventmanagement.intialactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmanagement.authentication.LoginActivity
import com.example.eventmanagement.authentication.SignUpActivity
import com.example.eventmanagement.databinding.ActivityHomeBinding
import com.example.eventmanagement.eventactivities.EventDisplayerActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.guestMode.setOnClickListener {
            val intent = Intent(this@HomeActivity,EventDisplayerActivity::class.java)
            startActivity(intent)
        }
        binding.loginButtonHome.setOnClickListener {
            val intent = Intent(this@HomeActivity,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.signUpButtonHome.setOnClickListener {
            val intent = Intent(this@HomeActivity,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}