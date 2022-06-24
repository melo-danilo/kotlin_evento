package com.example.kotlin_evento.request

import com.example.kotlin_evento.models.Event
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface EventApi {

    @GET("events/")
    fun getEvents(): Observable<List<Event>>

    @GET("events/{id_event}")
    fun getEvent(@Path("id_event") id: Int): Observable<Event>

}