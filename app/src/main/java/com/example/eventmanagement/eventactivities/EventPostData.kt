package com.example.eventmanagement.eventactivities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.eventmanagement.constants.Cookie
import com.example.eventmanagement.constants.CurrentUserRole
import com.example.eventmanagement.databinding.ActivityEventPostDataBinding
import com.example.eventmanagement.eventmodel.EventDTO
import com.example.eventmanagement.eventmodel.PostEventModel
import com.example.eventmanagement.retrofit.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime

class EventPostData : AppCompatActivity() {
    lateinit var binding: ActivityEventPostDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventPostDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateSelector.setOnClickListener {
            openDialogDate()
        }
        binding.timeSelector.setOnClickListener {
            openDialogTime()
        }
        binding.SubmitNewEvent.setOnClickListener {
            val eventTitle = binding.editTextTitle.text.toString()
            val eventDescription = binding.editTextDescription.toString()
            val eventLocation = binding.editTextLocation.toString()
            val time = binding.timeShower.text.toString()
            val date = binding.timeShower.text.toString()
            val eventFormLink :String? = null
            val localTime = LocalTime.now()
            val event  = PostEventModel(21,eventTitle,eventDescription,eventLocation,"2023-09-24","2023-09-24",
                localTime,localTime
                )
            val gson = Gson()
            val json = gson.toJson(event)
            Log.d("json", "$json   Date->     $localTime")
            submitNewEvent(event)
        }



    }

    private fun submitNewEvent(event: PostEventModel) {
        GlobalScope.launch {
            val apiService = RetrofitClient.create()
            val call = apiService.postEvent(event)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {


                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EventPostData,
                            "Event post Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("post","Doneeeeeeeeeeeeeeee")


                    }
                    else {



                        Toast.makeText(this@EventPostData, "Not Posting", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("post","NotDoneeeeeeeeeeeeeeee ,  code = "+response.code())
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("post","FailDoneeeeeeeeeeeeeeee")
                    Toast.makeText(this@EventPostData, "Something Went Wrong Post", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }


    }

    private fun openDialogTime() {
        val dialogTime = TimePickerDialog(this,

            { _, p1, p2 -> binding.timeShower.text = "$p1 : $p2" },12,0,true  )

        dialogTime.show()
    }

    private fun openDialogDate() {
        val currentDate = LocalDate.now()


        val dialogDate = DatePickerDialog(this,
            { _, p1, p2, p3 -> binding.dateShower.text = "$p1/$p2/$p3" }
            ,currentDate.year,currentDate.monthValue,currentDate.dayOfMonth)
        dialogDate.show()
    }



}