package com.example.kotlin_evento.models

data class Event (
    val id : Int,
    val title : String,
    val price : Double,
    val latitude : Float,
    val longitude : Float,
    val image : String,
    val description : String,
    val date : Long
)
