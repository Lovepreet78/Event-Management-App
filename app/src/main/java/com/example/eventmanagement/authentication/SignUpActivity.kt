package com.example.eventmanagement.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.eventmanagement.databinding.ActivitySignUpBinding
import com.example.eventmanagement.eventactivities.EventDisplayerActivity
import com.example.eventmanagement.retrofit.RetrofitClient
import com.example.eventmanagement.users.AuthUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signUpButton.setOnClickListener {
            if(binding.editTextUsernameSignUp.text.toString() == "" || binding.editTextUsernameSignUp.text==null){
                Toast.makeText(this@SignUpActivity, "Username Must not be Empty", Toast.LENGTH_SHORT).show()
            }
            else if(binding.editTextUsernameSignUp.text.toString().length<6){
                Toast.makeText(this@SignUpActivity, "Username Minimum length is 6", Toast.LENGTH_SHORT).show()
            }
            else if(binding.editTextPasswordSignUp.text.toString() == "" || binding.editTextPasswordSignUp.text==null){
                Toast.makeText(this@SignUpActivity, "Password Must not be Empty", Toast.LENGTH_SHORT).show()
            }
            else if(binding.editTextPasswordSignUp.text.toString().length<8){
                Toast.makeText(this@SignUpActivity, "Password Minimum length is 8", Toast.LENGTH_SHORT).show()
            }
            else if(binding.editTextUsernameSignUp.text.toString()==binding.editTextPasswordSignUp.text.toString()){
                Toast.makeText(this@SignUpActivity, "Username can't be same as your password", Toast.LENGTH_SHORT).show()
            }
            else if(binding.editTextConfirmPasswordSignUp.text.toString() == "" || binding.editTextConfirmPasswordSignUp.text==null){
                Toast.makeText(this@SignUpActivity, "Confirm Password Must not be Empty", Toast.LENGTH_SHORT).show()
            }
            else if(binding.editTextPasswordSignUp.text.toString() != binding.editTextConfirmPasswordSignUp.text.toString()){
                Toast.makeText(this@SignUpActivity, "Password Does not Match", Toast.LENGTH_SHORT).show()
            }

            else{
//                Toast.makeText(this@SignUpActivity, "Everything is Fine", Toast.LENGTH_SHORT).show()
                val userName = binding.editTextUsernameSignUp.text.toString()
                val userPassword = binding.editTextPasswordSignUp.text.toString()
                registerUser(userName,userPassword)
            }
        }
    }

    private fun registerUser(userName: String, userPassword: String) {
        val newUser = AuthUser(userName,userPassword)
        val apiService = RetrofitClient.create()
        val call  = apiService.registerNewUser(newUser)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SignUpActivity, "User Created Successfully", Toast.LENGTH_SHORT).show()
                    val intentToEvents = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intentToEvents)
                    finish()
                } else {
                    Toast.makeText(this@SignUpActivity, "User Name is already Taken", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        })

    }
}