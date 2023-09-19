package com.example.eventmanagement.eventactivities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import com.example.eventmanagement.R
import com.example.eventmanagement.databinding.ActivityEventPostDataBinding
import java.time.LocalDate

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