package com.example.kotlin_evento.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Evento (
    val id : Int,
    val title : String,
    val price : Double,
    val latitude : Double,
    val longitude : Double,
    val image : String,
    val description : String,
    val date : Double
)
