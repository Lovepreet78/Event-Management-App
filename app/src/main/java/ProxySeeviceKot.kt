//package com.example.eventmanagement.eventactivities
//
//import org.apache.http.client.methods.HttpPost
//import android.util.Log
//import com.example.eventmanagement.constants.Cookie
//import org.apache.http.client.config.RequestConfig
//import org.apache.http.client.methods.HttpPost
//import org.apache.http.conn.ssl.NoopHostnameVerifier
//import org.apache.http.entity.ContentType
//import org.apache.http.entity.InputStreamEntity
//import org.apache.http.impl.client.CloseableHttpClient
//import org.apache.http.impl.client.HttpClientBuilder
//import java.io.ByteArrayInputStream
//
//
//class ProxySeeviceKot {
//
//    companion object {
//        fun doPost() {
//            Log.e("Pornn", "firsttttttttttttttttttttttttttttttttttttttt")
//            val url = "https://eventmanagementplatform.onrender.com/user/events/post"
//
//
//            try {
//                Log.e("Pornn", "2ndirsttttttttttttttttttttttttttttttttttttttt")
//                val httpClient: CloseableHttpClient =
//                    HttpClientBuilder.create()
//                        .setDefaultRequestConfig(
//                            RequestConfig.DEFAULT
//                        )
//                        .build()
//
//                println("Cookie = ${Cookie.cookie}")
//                val post = HttpPost(url)
//                Log.e("Pornn", "justtttttttmediumttttttttttttttttttttttttttttttttttttttt")
//                post.addHeader("Cookie", Cookie.cookie)
//                Log.e("Pornn", "addheaderttttttttttttttttttttttttttttttttttttttt")
//                val respBody =
//                    "{\"ID\":\"99999\", \"title\":\"Lovepreet succesfull mime \", \"content\":\"a\", \"location\":\"a\", \"startDay\":\"2023-09-24\", \"endDay\":\"2023-09-28\", \"startTime\":\"10:12:04.748194\", \"endTime\":\"12:12:04.748265\"}"
//                post.entity = InputStreamEntity(
//                    ByteArrayInputStream(respBody.toByteArray()),
//                    ContentType.APPLICATION_JSON
//                )
//                Log.e("Pornn", "thirdttttttttttttttttttttttttttttttttttttttt")
//
//                val response = httpClient.execute(post)
////                println(
////                    "Response = ${
////                        EventPostData.convertStreamToString(
////                            response.entity.content,
////                            null
////                        )
////                    }"
////                )
//                Log.e("Pornnn", response.statusLine.statusCode.toString())
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
////
////                }
//            } catch (e: Exception) {
//                Log.e(
//                    "Pornnn",
//                    "jjjjjjjjjjjjj........................................................${e.message}"
//                )
//                throw RuntimeException(e)
//
//            }
//        }
//
//    }
//}