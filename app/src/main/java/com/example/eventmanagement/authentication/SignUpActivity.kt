package com.example.eventmanagement.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eventmanagement.R
import com.example.eventmanagement.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
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
                Toast.makeText(this@SignUpActivity, "Everything is Fine", Toast.LENGTH_SHORT).show()
            }
        }
    }
}