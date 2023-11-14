package com.example.eventmanagement.admin.eventManager

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.eventmanagement.databinding.ActivityAdminEditEventBinding
import com.example.eventmanagement.eventactivities.EventPostData
import com.example.eventmanagement.eventactivities.imageSelectorBitmap.uriToBitmap
import com.example.eventmanagement.eventmodel.EventPostDTO
import com.example.eventmanagement.eventmodel.LocalTimeConverter
import com.example.eventmanagement.retrofit.RetrofitClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eclipse.jetty.http.MultiPartFormInputStream.MultiPart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream


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
    lateinit var imageUri:Uri
    var bitmapImage : Bitmap?=null




    private var selectedImageUri: Uri? = null
    private var eventIdForImage :Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title ="Edit Event : Admin"

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
        eventIdForImage= eventId.toLong()

        val previousEvent = EventPostDTO(eventId,title,content,location,registrationLink,imageLink,startDay,endDay,startTime,endTime)


        val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
            imageUri  = it!!
        }

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

//        binding.imageSelector.setOnClickListener {
//            contract.launch("Image/*")
//            val selectedImageUri: Uri = imageString
//            val bitmap = uriToBitmap(this, selectedImageUri)
//
//            if (bitmap != null) {
//                bitmapImage = bitmap
////                binding.imageConfirmed.visibility = View.VISIBLE
//                var imageLink:String = imageToUrl(bitmapImage!!)
//            } else {
//
//                Toast.makeText(this, "Image Selection failed , try again", Toast.LENGTH_SHORT).show()
//            }
//        }



        updateItems(eventId,title,content,location,startTime,startDay,endTime,endDay,registrationLink,imageLink)
        binding.SubmitNewEvent.setOnClickListener {
            updateEvent(eventId,previousEvent)
        }


        binding.imageSelector.setOnClickListener {
            pickImage()
        }







    }
    val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Image successfully picked
                val data: Intent? = result.data
                selectedImageUri = data?.data
                // Now, you can upload the image
                uploadImage()
            }
        }
    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
    private fun getFile(context: Context, uri: Uri): File {
        val contentResolver = context.contentResolver
        val file = File(context.cacheDir, "temp_image_file")
        file.createNewFile()

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                val outputStream = FileOutputStream(file)
                outputStream.use { output ->
                    val buffer = ByteArray(4 * 1024) // buffer size
                    while (true) {
                        val byteCount = input.read(buffer)
                        if (byteCount < 0) break
                        output.write(buffer, 0, byteCount)
                    }
                    output.flush()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file
    }
    private fun uploadImage() {
        // Check if an image is selected
        selectedImageUri?.let { uri ->
            // Get the file name and create a request body
            val file = getFile(this, uri)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            // Call the API for image upload
            val apiService = RetrofitClient.create()
            val call: Call<Void> = apiService.postImageByAdmin(eventIdForImage, body)

            // Enqueue the call
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminEditEvent, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                        // Image uploaded successfully
                        Log.d("ImageUpload", "Image uploaded successfully")
                    } else {
                        // Handle the error
                        Log.e("ImageUpload", "Image upload failed. ${
                            response.errorBody()?.byteStream().toString()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Handle the failure
                    Log.e("ImageUpload", "Image upload failed!!. ${t.message}")
                }
            })
        }
    }


    private fun updateEvent(eventId: Long, previousEvent: EventPostDTO) {
        val title = binding.editTextTitle.text.toString()
        val content = binding.editTextDescription.text.toString()
        val location = binding.editTextLocation.text.toString()
        val registrationLink = binding.editTextEventLink.text.toString()
        val imageLink = previousEvent.imageLink()

        val startDay = if(isStartDateChanged) toSendStartDate else previousEvent.startDay()
        val endDay = if(isEndDateChanged) toSendEndDate else previousEvent.endDay()
        val startTime = if(isStartTimeChanged) toSendStartTime else previousEvent.startTime()
        val endTime = if(isEndTimeChanged) toSendEndTime else previousEvent.endTime()

        val editedEvent = EventPostDTO(eventId,title,content,location,registrationLink,imageLink,startDay.toString(),endDay.toString(),startTime.toString(),endTime.toString())
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
                                "Event Changed Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("adminEdit",editedEvent.toString())


                        }
                        else {



                            Toast.makeText(this@AdminEditEvent, "Something Wrong", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("adminEdit","NotDoneeeeeeeeeeeeeeee ${response.errorBody()?.byteStream()
                                ?.let { EventPostData.convertStreamToString(it) }}\n  $response")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("adminEdit","FailDoneeeeeeeeeeeeeeee $call")
                        Toast.makeText(this@AdminEditEvent, "Something Went Wrong!!", Toast.LENGTH_SHORT)
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

//    fun uploadImage(){
//        var filesDir = applicationContext.filesDir
//        var file = File(filesDir,"Image.png")
//        val inputStream = contentResolver.openInputStream(imageUri)
//        val outputStream = FileOutputStream(file)
//        inputStream!!.copyTo(outputStream)
//        val mediaType = "image/*".toMediaTypeOrNull()
//        val requestBody = asRe
//
//        var p = MultipartBody.Part.createFormData("profile",file.name,requestBody)
//    }




}