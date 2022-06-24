package com.example.kotlin_evento.utils

import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


object Convert {

    fun getTimestampToDate(timestamp : Long): String {
        val timeD = Date(timestamp * 1000)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())

        return sdf.format(timeD)
    }

    fun getPriceReal(price : Double): String {
        return NumberFormat.getCurrencyInstance().format(price);
    }

    fun isValidEmail(email : String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}