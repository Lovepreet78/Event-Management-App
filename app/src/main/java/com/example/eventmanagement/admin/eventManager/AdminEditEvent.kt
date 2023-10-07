package com.example.eventmanagement.admin.eventManager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.eventmanagement.databinding.ActivityAdminEditEventBinding
import com.example.eventmanagement.eventactivities.EventPostData
import com.example.eventmanagement.eventmodel.EventPostDTO
import com.example.eventmanagement.eventmodel.LocalTimeConverter
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime

class AdminEditEvent : AppCompatActivity() {
    lateinit var binding: ActivityAdminEditEventBinding
    var toSendStartTime: LocalTime = LocalTime.now()
    var toSendStartDate:LocalDate= LocalDate.now()
    var toSendEndTime: LocalTime = LocalTime.now()
    var toSendEndDate:LocalDate = LocalDate.now()
    var isStartTimeChanged :Boolean = false
    var isEndTimeChanged :Boolean = false
    var isStartDateChanged :Boolean = false
    var isEndDateChanged :Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentFromEdit = intent
        val eventId  = intentFromEdit.getLongExtra("eventId",0)
        val title = intentFromEdit.getStringExtra("title")
        val content = intentFromEdit.getStringExtra("content")
        val location = intentFromEdit.getStringExtra("location")
        val startDay = intentFromEdit.getStringExtra("startDay")
        val endDay = intentFromEdit.getStringExtra("endDay")
        val startTime = intentFromEdit.getStringExtra("startTime")
        val endTime = intentFromEdit.getStringExtra("endTime")
        val previousEvent = EventPostDTO(eventId,title,content,location,startDay,endDay,startTime,endTime)



        binding.startDateSelector.setOnClickListener {
            openStartDialogDate()
            isStartDateChanged = true
        }
        binding.startTimeSelector.setOnClickListener {
            openStartDialogTime()
            isStartTimeChanged=true
        }
        binding.endDateSelector.setOnClickListener {
            openEndDialogDate()
            isEndDateChanged=true
        }
        binding.endTimeSelector.setOnClickListener {
            openEndDialogTime()
            isEndTimeChanged=true
        }



        updateItems(eventId,title,content,location,startTime,startDay,endTime,endDay)
        binding.SubmitNewEvent.setOnClickListener {
            updateEvent(eventId,previousEvent)
        }

    }

    private fun updateEvent(eventId: Long, previousEvent: EventPostDTO) {
        val title = binding.editTextTitle.text.toString()
        val content = binding.editTextDescription.text.toString()
        val location = binding.editTextLocation.text.toString()
        val startDay = if(isStartDateChanged) toSendStartDate else previousEvent.startDay()
        val endDay = if(isEndDateChanged) toSendEndDate else previousEvent.endDay()
        val startTime = if(isStartTimeChanged) toSendStartTime else previousEvent.startTime()
        val endTime = if(isEndTimeChanged) toSendEndTime else previousEvent.endTime()

        val editedEvent = EventPostDTO(eventId,title,content,location,startDay.toString(),endDay.toString(),startTime.toString(),endTime.toString())
        changeEventByAdmin(eventId,editedEvent)
    }

    private fun changeEventByAdmin(eventId: Long, editedEvent: EventPostDTO) {
            GlobalScope.launch {
                val apiService = RetrofitClient.create()
                val call = apiService.editEventByAdmin(eventId,editedEvent)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {


                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@AdminEditEvent,
                                "Event post Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("adminEdit",editedEvent.toString())


                        }
                        else {



                            Toast.makeText(this@AdminEditEvent, "Not Posting $response", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("adminEdit","NotDoneeeeeeeeeeeeeeee ${response.errorBody()?.byteStream()
                                ?.let { EventPostData.convertStreamToString(it) }}\n  $editedEvent")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("adminEdit","FailDoneeeeeeeeeeeeeeee $call")
                        Toast.makeText(this@AdminEditEvent, "Something Went Wrong Post", Toast.LENGTH_SHORT)
                            .show()

                    }
                })
            }




    }

    private fun updateItems(
        eventId: Long,
        title: String?,
        content: String?,
        location: String?,
        startTime: String?,
        startDay: String?,
        endTime: String?,
        endDay: String?
    ) {
        binding.editTextTitle.setText(title)
        binding.editTextDescription.setText(content)
        binding.editTextLocation.setText(location)
        binding.startTimeShower.text = startTime
        binding.endTimeShower.text = endTime
        binding.startDateShower.text = startDay
        binding.endDateShower.text = endDay


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




}