package com.example.kotlin_evento.request

import com.example.kotlin_evento.models.Evento
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface EventoApi {

    @GET("events/")
    fun getEvents(): Observable<List<Evento>>

    @GET("events/{id_evento}")
    fun getEvent(@Path("id_evento") id: Int): Call<Evento>

}