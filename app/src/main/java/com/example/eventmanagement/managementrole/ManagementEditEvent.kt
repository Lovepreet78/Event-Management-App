package com.example.eventmanagement.managementrole

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.eventmanagement.R
import com.example.eventmanagement.databinding.ActivityAdminEditEventBinding
import com.example.eventmanagement.databinding.ActivityManagementEditEventBinding
import com.example.eventmanagement.eventactivities.EventPostData
import com.example.eventmanagement.eventmodel.EventPostDTO
import com.example.eventmanagement.eventmodel.LocalTimeConverter
import com.example.eventmanagement.imageUploader.ImageUploader1
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalTime

class ManagementEditEvent : AppCompatActivity() {
    lateinit var binding: ActivityManagementEditEventBinding
    var toSendStartTime: LocalTime = LocalTime.now()
    var toSendStartDate: LocalDate = LocalDate.now()
    var toSendEndTime: LocalTime = LocalTime.now()
    var toSendEndDate: LocalDate = LocalDate.now()
    var isStartTimeChanged :Boolean = false
    var isEndTimeChanged :Boolean = false
    var isStartDateChanged :Boolean = false
    var isEndDateChanged :Boolean = false

    private lateinit var imgUri: Uri
    private var imageUrl =""
    private var job: Job?=null
    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
        imgUri = it!!
        if(imgUri!=null) {
            binding.imageViewAdminEdit.setImageURI(imgUri)
            binding.imageLayout.visibility = View.VISIBLE
        }
        else{
            Toast.makeText(this, "Image can't be selected", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementEditEventBinding.inflate(layoutInflater)
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
        val registrationLink = intentFromEdit.getStringExtra("registrationLink")
        val imageLink = intentFromEdit.getStringExtra("imageLink")
        val previousEvent = EventPostDTO(eventId,title,content,location,registrationLink,imageLink,startDay,endDay,startTime,endTime)
        updateItems(eventId,title,content,location,startTime,startDay,endTime,endDay,registrationLink,imageLink)

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

        binding.imageSelector.setOnClickListener {
            contract.launch("image/*")
        }
        binding.imageConfirmed.setOnClickListener {
            val imageUploader = ImageUploader1()
//            var path = imageUploader.getImageFilePath(this,imgUri)
            var path = getRealPathFromURI(imgUri!!,this)
            if(path!=null){
                job?.cancel()
                binding.imageProgressBar.visibility= View.VISIBLE


                job = CoroutineScope(Dispatchers.Main).launch{
                    withContext(Dispatchers.IO){

                        imageUploader.uploadImage(path!!)
                        imageUrl = imageUploader.getImageUrl()
                        Log.d("qqqqqqq",imageUrl)

                    }

                    binding.imageProgressBar.visibility= View.GONE

                }



            }
            else{
                Toast.makeText(this, "Image can't be selected $path $imgUri", Toast.LENGTH_SHORT).show()
            }

        }


        binding.SubmitNewEvent.setOnClickListener {
            updateEvent(eventId,previousEvent)
        }
    }
    private fun updateEvent(eventId: Long, previousEvent: EventPostDTO) {
        val title = binding.editTextTitle.text.toString()
        val content = binding.editTextDescription.text.toString()
        val location = binding.editTextLocation.text.toString()
        val registrationLink = binding.editTextEventLink.text.toString()
        val imageLink = if(imageUrl!="") imageUrl else previousEvent.imageLink()

        val startDay = if(isStartDateChanged) toSendStartDate else previousEvent.startDay()
        val endDay = if(isEndDateChanged) toSendEndDate else previousEvent.endDay()
        val startTime = if(isStartTimeChanged) toSendStartTime else previousEvent.startTime()
        val endTime = if(isEndTimeChanged) toSendEndTime else previousEvent.endTime()

        val editedEvent = EventPostDTO(eventId,title,content,location,registrationLink,imageLink,startDay.toString(),endDay.toString(),startTime.toString(),endTime.toString())
        changeEventByManagement(eventId,editedEvent)
    }
    private fun changeEventByManagement(eventId: Long, editedEvent: EventPostDTO) {
        GlobalScope.launch {
            val apiService = RetrofitClient.create()
            val call = apiService.editManagementEvent(eventId,editedEvent)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {


                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ManagementEditEvent,
                            "Event Changed Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("adminEdit",editedEvent.toString())


                    }
                    else {



//                        Toast.makeText(this@ManagementEditEvent, "Not Posting $response", Toast.LENGTH_SHORT)
//                            .show()
                        Toast.makeText(this@ManagementEditEvent, "Something is Filled Wrong, Check Carefully", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("adminEdit","NotDoneeeeeeeeeeeeeeee ${response.errorBody()?.byteStream()
                            ?.let { EventPostData.convertStreamToString(it) }}\n  $editedEvent")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("adminEdit","FailDoneeeeeeeeeeeeeeee $call")
                    Toast.makeText(this@ManagementEditEvent, "Something Went Wrong !!", Toast.LENGTH_SHORT)
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
        endDay: String?,
        registrationLink:String?,
        imageLink:String?
    ) {
        binding.editTextTitle.setText(title)
        binding.editTextDescription.setText(content)
        binding.editTextLocation.setText(location)
        binding.startTimeShower.text = startTime
        binding.endTimeShower.text = endTime
        binding.startDateShower.text = startDay
        binding.endDateShower.text = endDay
        binding.editTextEventLink.setText(registrationLink)


    }





    private fun openStartDialogTime() {
        val dialogTime = TimePickerDialog(this,

            { _, p1, p2 -> run{
                val startLocalTime= LocalTimeConverter.convertStringTOLocalTIme(p1,p2,0,0,0)
                Log.d("localtest1",startLocalTime.toString())
                toSendStartTime = startLocalTime
                binding.startTimeShower.text = startLocalTime.toString()


            } },12,0,true  )

        dialogTime.show()
    }

    private fun openStartDialogDate() {
        val currentDate = LocalDate.now()


        val dialogDate = DatePickerDialog(this,
            { _, p1, p2, p3 -> run{ }
                toSendStartDate = LocalTimeConverter.intToLocalDate(p1,p2,p3)
                binding.startDateShower.text = toSendStartDate.toString()

            }
            ,currentDate.year,currentDate.monthValue,currentDate.dayOfMonth)
        dialogDate.show()
    }
    private fun openEndDialogTime() {
        val dialogTime = TimePickerDialog(this,

            { _, p1, p2 -> run{
                val startLocalTime= LocalTimeConverter.convertStringTOLocalTIme(p1,p2,0,0,0)
                Log.d("localtest1",startLocalTime.toString())
                toSendEndTime = startLocalTime
                binding.endTimeShower.text = toSendEndTime.toString()
            } },12,0,true  )

        dialogTime.show()
    }

    private fun openEndDialogDate() {
        val currentDate = LocalDate.now()


        val dialogDate = DatePickerDialog(this,
            { _, p1, p2, p3 -> run{
                toSendEndDate = LocalTimeConverter.intToLocalDate(p1,p2,p3)
                binding.endDateShower.text = toSendEndDate.toString()
            }}
            ,currentDate.year,currentDate.monthValue,currentDate.dayOfMonth)
        dialogDate.show()
    }
    fun getRealPathFromURI(uri: Uri, context: Context): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex =  returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available() ?: 0
            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream?.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)

        } catch (e: java.lang.Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path
    }
}