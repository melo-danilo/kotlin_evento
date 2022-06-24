package com.example.kotlin_evento.request

import com.example.kotlin_evento.utils.Connection
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ServiceConnect {

    companion object {

        private val httpClient: OkHttpClient by lazy {
            OkHttpClient()
                .newBuilder()
                .build()
        }

        fun getInstance() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(Connection.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }


}