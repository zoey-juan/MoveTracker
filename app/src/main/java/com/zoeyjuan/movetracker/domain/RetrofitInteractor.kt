package com.zoeyjuan.movetracker.domain

import com.zoeyjuan.movetracker.database.UserLocation
import com.zoeyjuan.movetracker.network.APIPostInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RetrofitInteractor {
    private val BASEURL: String = "https://www.xxx.com/"
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client =  OkHttpClient.Builder().addInterceptor(interceptor).build();
    private val retrofit = Retrofit.Builder().baseUrl(BASEURL).client(client).build()
    private val service = retrofit.create(APIPostInterface::class!!.java)

    fun uploadTrack(list: List<UserLocation>?) {
        // TODO: upload and responds
        //service.uploadTrack()
    }
}