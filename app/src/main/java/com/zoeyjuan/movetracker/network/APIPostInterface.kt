package com.zoeyjuan.movetracker.network

import com.zoeyjuan.movetracker.database.UserLocation
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIPostInterface {
    @FormUrlEncoded
    @POST("track")
    fun uploadTrack(
            @Field("timeStamp") timeStamp: Long,
            @Field("user_uuids[]") user_uuids: ArrayList<UserLocation>): Call<ResponseBody>
}