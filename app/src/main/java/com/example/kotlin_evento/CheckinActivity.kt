package com.example.kotlin_evento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.evento_detalhado.view.*

class CheckinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)

        val id = intent.extras?.get("id_evento")
        val url : String = intent.extras?.get("url_image") as String
        val title = intent.extras?.get("title_evento")

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.ic_picasso_placeholder)
            .error(R.drawable.ic_picasso_not_suported)
            .into(image_evento)
    }
}