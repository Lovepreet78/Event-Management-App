package com.example.eventmanagement.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.eventmanagement.constants.Cookie
import com.example.eventmanagement.constants.CurrentUserRole

import com.example.eventmanagement.databinding.ActivityLoginBinding
import com.example.eventmanagement.eventactivities.EventDisplayerActivity
import com.example.eventmanagement.eventactivities.EventPostData
import com.example.eventmanagement.retrofit.RetrofitClient
import com.example.eventmanagement.users.AuthUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            if (binding.usernameEditTextLogin.text.toString() == "" || binding.usernameEditTextLogin.text == null) {
                Toast.makeText(this@LoginActivity, "Username Must not be Empty", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.passwordEditTextLogin.text.toString() == "" || binding.passwordEditTextLogin.text == null) {
                Toast.makeText(this@LoginActivity, "Password Must not be Empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
//                Toast.makeText(this@LoginActivity, "Everything is Fine", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@LoginActivity,EventPostData::class.java)
//                startActivity(intent)
                binding.loginProgressBar.visibility = View.VISIBLE

                val userName = binding.usernameEditTextLogin.text.toString()
                val password = binding.passwordEditTextLogin.text.toString()
                Log.d("Love", "$userName $password")
                loginUser(userName, password)

            }

        }
    }

    private fun loginUser(userName: String, password: String) {


        GlobalScope.launch {
            val apiService = RetrofitClient.create()
            val call = apiService.loginUser(userName, password)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {


                    if (response.isSuccessful) {
                            Toast.makeText(
                                this@LoginActivity,
                                "User Login Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val cookie = response.headers().get("Set-cookie").toString()
                            val userRole = response.headers().get("roles").toString()

                            Cookie.cookie = cookie
                            CurrentUserRole.currentUserRole = userRole

                            val intentToEvents =
                                Intent(this@LoginActivity, EventDisplayerActivity::class.java)
                            startActivity(intentToEvents)
                            binding.loginProgressBar.visibility = View.GONE
                            finish()

                    }
                    else {
                        Log.d("Love",response.code().toString())
                        binding.loginProgressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Wrong Credential!!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    binding.loginProgressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Something Went Wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }


    }
}