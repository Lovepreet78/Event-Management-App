package com.example.eventmanagement.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

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

                loginUser(userName, password)

            }

        }
    }

    private fun loginUser(userName: String, password: String) {

        val user = AuthUser(userName, password)
        GlobalScope.launch {
            val apiService = RetrofitClient.create()
            val call = apiService.loginUser(user)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    if (response.isSuccessful) {
//                    Toast.makeText(
//                        this@LoginActivity,
//                        "User Login Successfully",
//                        Toast.LENGTH_SHORT
//                    ).show()
                        val iss = response.headers().get("Location").toString().contains("events")
                        val logTest = response.headers().get("Location").toString()
                        Log.d("$logTest  iss->  $iss","lovepreet")
                        if (iss) {
                            val cookie = response.headers().get("Set-cookie").toString()
                            val intentToEvents =
                                Intent(this@LoginActivity, EventDisplayerActivity::class.java)
                            startActivity(intentToEvents)
                            binding.loginProgressBar.visibility = View.GONE
                            finish()
                        } else {
                            binding.loginProgressBar.visibility = View.GONE
                            Toast.makeText(this@LoginActivity, "Wrong Credential", Toast.LENGTH_SHORT)
                                .show()

                        }
                    } else {
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