package com.example.eventmanagement.intialactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.eventmanagement.authentication.LoginActivity
import com.example.eventmanagement.authentication.SignUpActivity
import com.example.eventmanagement.constants.Cookie
import com.example.eventmanagement.constants.CurrentUserRole
import com.example.eventmanagement.databinding.ActivityHomeBinding
import com.example.eventmanagement.eventactivities.EventDisplayerActivity
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        binding.savedLoginButtonHome.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val savedUsername = sharedPreferences.getString("username", null)
            val savedPassword = sharedPreferences.getString("password", null)

            if (savedUsername != null && savedPassword != null) {
                if(savedUsername.isNotBlank() && savedPassword.isNotEmpty()){
                    loginUser(savedUsername,savedPassword)
                }
                else{

                    Toast.makeText(this@HomeActivity, "No Saved Login Info", Toast.LENGTH_SHORT).show()
                }
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
                            this@HomeActivity,
                            "User Login Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val cookie = response.headers().get("Set-cookie").toString()
                        val userRole = response.headers().get("roles").toString()

                        Cookie.cookie = cookie
                        Log.d("currfuck", CurrentUserRole.currentUserRole!!.toString())
                        CurrentUserRole.currentUserRole = userRole

                        val intentToEvents =
                            Intent(this@HomeActivity, EventDisplayerActivity::class.java)
                        startActivity(intentToEvents)

                        finish()

                    }
                    else {
                        Log.d("Love",response.code().toString())

                        Toast.makeText(this@HomeActivity, "Wrong Credential!!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                    Toast.makeText(this@HomeActivity, "Something Went Wrong", Toast.LENGTH_SHORT)
                        .show()
                    throw t
                }
            })
        }


    }
}