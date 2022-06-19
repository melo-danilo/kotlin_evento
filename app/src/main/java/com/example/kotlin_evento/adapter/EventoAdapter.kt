package com.example.kotlin_evento.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_evento.R
import com.example.kotlin_evento.models.Evento
import com.example.kotlin_evento.utils.OnClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.evento_item.view.*
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class EventoAdapter (private val onClickListener: OnClickListener) : RecyclerView.Adapter<EventoAdapter.ViewHolder>() {

    var listEventos = ArrayList<Evento>()
    var onClick : OnClickListener = onClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("EventoAdapter", "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.evento_item, parent, false)
        return ViewHolder(inflater, this.onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("EventoAdapter", "onBindViewHolder")
        holder.bind(listEventos[position])
    }

    override fun getItemCount(): Int {
        Log.d("EventoAdapter", "getItemCount")
        return listEventos.size
    }

    fun getItem(position: Int): Evento {
        return listEventos[position]
    }

    class ViewHolder(view : View, onClickListener: OnClickListener) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.title_evento
        private val date: TextView = view.data_evento
        private val price: TextView = view.preco_evento
        private val image: ImageView = view.image_evento

        init {
            view.setOnClickListener(View.OnClickListener {
                onClickListener.onClickItemClicked(adapterPosition)
            })
        }

        fun bind(data : Evento) {
            Log.d("EventoAdapter", "ViewHolder")
            title.text = data.title
            date.text = data.date.toString()
            price.text = "R$ ${data.price}"

            Picasso.get()
                .load(data.image)
                .placeholder(R.drawable.ic_picasso_placeholder)
                .error(R.drawable.ic_picasso_not_suported)
                .into(image)

        }
    }

}