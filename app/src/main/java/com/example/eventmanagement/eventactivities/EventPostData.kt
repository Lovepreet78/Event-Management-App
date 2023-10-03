package com.example.eventmanagement.eventactivities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.eventmanagement.databinding.ActivityEventPostDataBinding
import com.example.eventmanagement.eventmodel.PostEventModel
import com.example.eventmanagement.retrofit.RetrofitClient
import com.example.eventmanagement.retrofit.testing.TestClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.Charset
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
//            val eventTitle = binding.editTextTitle.text.toString()
//            val eventDescription = binding.editTextDescription.text.toString()
//            val eventLocation = binding.editTextLocation.text.toString()
//            val time = binding.timeShower.text.toString()
//            val date = binding.timeShower.text.toString()
//            val eventFormLink :String? = null
//            val localTime = LocalTime.now()
//            val event  = PostEventModel(21,eventTitle,eventDescription,eventLocation,"2023-09-24","2023-09-28",
//                localTime, LocalTime.now().plusHours(2)
//                )
//            val gson = Gson()
//            val json = gson.toJson(event)

//            Log.d("json", "$json   Date->     $localTime")
//            submitNewEvent(event)
//            val proxyService = ProxyService()
//            proxyService.doPost()

//                testSubmit()
//            ProxySeeviceKot.doPost()
        }




    }


    private fun testSubmit(){
        val testApi = TestClient.create()

        // Create an instance of PostEventModel with your data
        val eventModel = PostEventModel(
            ID = 1, // Replace with your ID
            title = "Your Title",
            content = "Your Content",
            location = "Your Location",
            startDay = "Start Day",
            endDay = "End Day",
            startTime = LocalTime.now(), // Replace with your startTime
            endTime = LocalTime.now().plusHours(2) // Replace with your endTime
        )

        // Make the POST request
        val call = testApi.testPostEvent(eventModel)

        // Execute the call asynchronously
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // Handle successful response here
                    val responseBody = response.body()
                    Log.d("TestPost","Done..............")
                } else {
                    Log.d("TestPost","Fir se Error..............${
                        response.errorBody()?.byteStream()
                            ?.let { convertStreamToString(it) }
                    }\n"                             )
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("TestPost","Failure...........")
            }
        })
    }



    private fun submitNewEvent(event: PostEventModel) {
        GlobalScope.launch {
            val apiService = RetrofitClient.create()
            val call = apiService.postEvent(event)
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {


                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EventPostData,
                            "Event post Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("post","Doneeeeeeeeeeeeeeee")


                    }
                    else {



                        Toast.makeText(this@EventPostData, "Not Posting $response", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("post","NotDoneeeeeeeeeeeeeeee ${response.errorBody()?.byteStream()
                            ?.let { convertStreamToString(it) }}\n  $event")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("posttt","FailDoneeeeeeeeeeeeeeee")
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

    companion object{
        fun convertStreamToString(
            inputStream: InputStream,
            charset: Charset? = Charsets.UTF_8
        ): String {
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int

            while (inputStream.read(buffer).also { length = it } != -1) {
                result.write(buffer, 0, length)
            }

            return String(result.toByteArray())
        }
    }




}