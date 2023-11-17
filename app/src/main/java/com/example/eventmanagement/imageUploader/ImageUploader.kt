package com.example.eventmanagement.imageUploader

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import com.example.eventmanagement.eventactivities.EventPostData
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.IOException

class ImageUploader1 {
    private val apiKey = "6d207e02198a847aa98d0a2a901485a5"
    private val apiUrl = "https://freeimage.host/api/1/upload"
    var imageUrlt = ""

    fun getImageUrl(): String {
        return imageUrlt
    }
    fun uploadImage(imageFilePath: String)  {

        val file = File(imageFilePath)

        try {
            val base64Image = encodeImageToBase64(file)

            val client = OkHttpClient()

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", apiKey)
                .addFormDataPart("action", "upload")
                .addFormDataPart("source", base64Image)
                .addFormDataPart("format", "json")
                .build()


            val request = Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .build()


//            client.newCall(request).enqueue(object : okhttp3.Callback{
//                override fun onFailure(call: okhttp3.Call, e: IOException) {
//                    Log.d("qqqqqq","image Failed")
////                Toast.makeText(this, "Image u failed!!", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body?.string()
//
//                        // Parse the JSON response
//                        val jsonResponse = JSONObject(responseBody)
//
//                        // Extract the image URL
//                        var imageUrl = jsonResponse.getJSONObject("image").getString("url")
//
//                        // Now 'imageUrl' contains the URL of the uploaded image
//                        imageUrlt = imageUrl
//                        Log.d("qqqqqq", "Image Passed. URL: $imageUrl-$imageUrlt")
//                        // Handle the image URL as needed
//
//                    }
//                    else{
//                        Log.d("qqqqqq",response.message+"          "+response.body!!.byteStream()?.let {
//                            EventPostData.convertStreamToString(
//                                it
//                            )
//                        })
//                    }
//
//                }
//
//            })

            val rp = client.newCall(request).execute()
            if(rp.isSuccessful){
                val responseBody = rp.body?.string()

                // Parse the JSON response
                val jsonResponse = JSONObject(responseBody)

                // Extract the image URL
                var imageUrl = jsonResponse.getJSONObject("image").getString("url")

                // Now 'imageUrl' contains the URL of the uploaded image
                imageUrlt = imageUrl
                Log.d("qqqqqq", "Image Passed. URL: $imageUrl-$imageUrlt")
            }


        }
        catch (e: Exception) {
            Log.e("qqqqqq", "Exception encoding image to base64: ${e.message}")
        }

    }

    private fun encodeImageToBase64(file: File): String {
        val bytes = file.readBytes()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
    fun getImageFilePath(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null

        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return null
    }
}
