package com.example.eventmanagement.intialactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.eventmanagement.R
import com.example.eventmanagement.constants.Cookie
import com.example.eventmanagement.constants.CurrentUserRole
import com.example.eventmanagement.databinding.ActivitySplashBinding
import com.example.eventmanagement.eventactivities.EventDisplayerActivity
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setup()

    }

    private fun setup() {

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", null)
        val savedPassword = sharedPreferences.getString("password", null)

        if (savedUsername != null && savedPassword != null) {
            loginUser(savedUsername, savedPassword)
        } else {
            toHome()
        }
    }

    private fun toHome() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", null)
        editor.putString("password", null)
        editor.apply()
        val intentToHomeScreen = Intent(this@SplashActivity, HomeActivity::class.java)
        startActivity(intentToHomeScreen)
        finish()
    }

    private fun loginUser(userName: String, password: String) {


        GlobalScope.launch {
            val apiService = RetrofitClient.create()
            val call = apiService.loginUser(userName, password)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {


                    if (response.isSuccessful) {

                        val cookie = response.headers().get("Set-cookie").toString()
                        val userRole = response.headers().get("roles").toString()

                        Cookie.cookie = cookie

                        CurrentUserRole.currentUserRole = userRole

                        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("username", userName)
                        editor.putString("password", password)
                        editor.apply()

                        val intentToEvents =
                            Intent(this@SplashActivity, EventDisplayerActivity::class.java)
                        startActivity(intentToEvents)

                        finish()

                    }
                    else {
                        toHome()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    toHome()

                }
            })
        }


    }
}