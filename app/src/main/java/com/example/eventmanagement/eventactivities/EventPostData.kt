package com.example.eventmanagement.eventactivities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.eventmanagement.databinding.ActivityEventPostDataBinding
import com.example.eventmanagement.eventmodel.EventPostDTO
import com.example.eventmanagement.eventmodel.LocalTimeConverter
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
import java.time.format.DateTimeFormatter

class EventPostData : AppCompatActivity() {
    lateinit var binding: ActivityEventPostDataBinding
    var toSendStartTime:LocalTime = LocalTime.now()
    var toSendStartDate:LocalDate= LocalDate.now()
    var toSendEndTime:LocalTime = LocalTime.now()
    var toSendEndDate:LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventPostDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startDateSelector.setOnClickListener {
            openStartDialogDate()
        }
        binding.startTimeSelector.setOnClickListener {
            openStartDialogTime()
        }
        binding.endDateSelector.setOnClickListener {
            openEndDialogDate()
        }
        binding.endTimeSelector.setOnClickListener {
            openEndDialogTime()
        }
        binding.SubmitNewEvent.setOnClickListener {
            val eventTitle = binding.editTextTitle.text.toString()
            val eventDescription = binding.editTextDescription.text.toString()
            val eventLocation = binding.editTextLocation.text.toString()

            val startDate = binding.startDateShower.text.toString()

            val endDate = binding.endDateShower.text.toString()




            val ttt = LocalTime.now()
            val ttt2 = LocalTime.now().plusHours(2)

            val e1 = EventPostDTO(100,eventTitle,eventDescription,eventLocation,toSendStartDate.toString(),toSendEndDate.toString(),toSendStartTime.toString(),toSendEndTime.toString());


            submitNewEvent(e1)
        }




    }





    private fun submitNewEvent(event: EventPostDTO) {
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
                        Log.d("posttest",event.toString())


                    }
                    else {



                        Toast.makeText(this@EventPostData, "Not Posting $response", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("posttest","NotDoneeeeeeeeeeeeeeee ${response.errorBody()?.byteStream()
                            ?.let { convertStreamToString(it) }}\n  $event")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("posttt","FailDoneeeeeeeeeeeeeeee $call")
                    Toast.makeText(this@EventPostData, "Something Went Wrong Post", Toast.LENGTH_SHORT)
                        .show()
                    throw t
                }
            })
        }


    }

    private fun openStartDialogTime() {
        val dialogTime = TimePickerDialog(this,

            { _, p1, p2 -> run{ binding.startTimeShower.text = "$p1:$p2"
                val startLocalTime= LocalTimeConverter.convertStringTOLocalTIme(p1,p2,0,0,0)
                Log.d("localtest1",startLocalTime.toString())
                toSendStartTime = startLocalTime


            } },12,0,true  )

        dialogTime.show()
    }

    private fun openStartDialogDate() {
        val currentDate = LocalDate.now()


        val dialogDate = DatePickerDialog(this,
            { _, p1, p2, p3 -> run{ binding.startDateShower.text = "$p1-$p2-$p3" }
                toSendStartDate = LocalTimeConverter.intToLocalDate(p1,p2,p3)

            }
            ,currentDate.year,currentDate.monthValue,currentDate.dayOfMonth)
        dialogDate.show()
    }
    private fun openEndDialogTime() {
        val dialogTime = TimePickerDialog(this,

            { _, p1, p2 -> run{ binding.endTimeShower.text = "$p1:$p2"
                val startLocalTime= LocalTimeConverter.convertStringTOLocalTIme(p1,p2,0,0,0)
                Log.d("localtest1",startLocalTime.toString())
                toSendEndTime = startLocalTime
            } },12,0,true  )

        dialogTime.show()
    }

    private fun openEndDialogDate() {
        val currentDate = LocalDate.now()


        val dialogDate = DatePickerDialog(this,
            { _, p1, p2, p3 -> run{ binding.endDateShower.text = "$p1-$p2-$p3"
                toSendEndDate = LocalTimeConverter.intToLocalDate(p1,p2,p3)
            }}
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