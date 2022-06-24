package com.example.kotlin_evento.request

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface CheckinApi {

    @FormUrlEncoded
    @POST("checkin/")
    fun postCheckin(
        @Field("eventId") userId: String,
        @Field("name") nome: String,
        @Field("email") email: String
    ): Observable<ResponseBody>
}