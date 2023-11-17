package com.example.eventmanagement.eventactivities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.eventmanagement.databinding.ActivityEventPostDataBinding
import com.example.eventmanagement.eventactivities.imageSelectorBitmap.uriToBitmap
import com.example.eventmanagement.eventmodel.EventPostDTO
import com.example.eventmanagement.eventmodel.LocalTimeConverter
import com.example.eventmanagement.eventmodel.PostEventModel
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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


    private lateinit var imgUri:Uri
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
        binding = ActivityEventPostDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title ="Post New Event"

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
            val eventTitle = binding.editTextTitle.text.toString()
            val eventDescription = binding.editTextDescription.text.toString()
            val eventLocation = binding.editTextLocation.text.toString()

            val startDate = binding.startDateShower.text.toString()

            val endDate = binding.endDateShower.text.toString()

            val imageLink: String = imageUrl
//            val enrollmentLink: String? = binding.registrtionLink.text.toString()
            val enrollmentLink: String = binding.editTextEventLink.text.toString()



            val e1 = EventPostDTO(100,eventTitle,eventDescription,eventLocation,enrollmentLink,imageLink,toSendStartDate.toString(),toSendEndDate.toString(),toSendStartTime.toString(),toSendEndTime.toString());
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



                    }
                    else {



                        Toast.makeText(this@EventPostData, "Something is Filled Wrong, Check Carefully", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("posttest","NotDoneeeeeeeeeeeeeeee ${response.errorBody()?.byteStream()
                            ?.let { convertStreamToString(it) }}\n  $event")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("posttt","FailDoneeeeeeeeeeeeeeee $call")
                    Toast.makeText(this@EventPostData, "Something Went Wrong Post", Toast.LENGTH_SHORT)
                        .show()
                    throw t
                }
            })
        }


    }

    private fun openStartDialogTime() {
        val dialogTime = TimePickerDialog(this,

            { _, p1, p2 -> run{
                val startLocalTime= LocalTimeConverter.convertStringTOLocalTIme(p1,p2,0,0,0)

                toSendStartTime = startLocalTime
                binding.startTimeShower.text = startLocalTime.toString()


            } },12,0,true  )

        dialogTime.show()
    }

    private fun openStartDialogDate() {
        val currentDate = LocalDate.now()


        val dialogDate = DatePickerDialog(this,
            { _, p1, p2, p3 -> run{  }
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