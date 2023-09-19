package com.example.eventmanagement.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.eventmanagement.databinding.ActivityLoginBinding
import com.example.eventmanagement.eventactivities.EventPostData


class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            if(binding.usernameEditTextLogin.text.toString() == "" || binding.usernameEditTextLogin.text==null){
                Toast.makeText(this@LoginActivity, "Username Must not be Empty", Toast.LENGTH_SHORT).show()
            }
            else if(binding.passwordEditTextLogin.text.toString() == "" || binding.passwordEditTextLogin.text==null){
                Toast.makeText(this@LoginActivity, "Password Must not be Empty", Toast.LENGTH_SHORT).show()
            }

            else{
//                Toast.makeText(this@LoginActivity, "Everything is Fine", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity,EventPostData::class.java)
                startActivity(intent)
            }

        }
    }
}