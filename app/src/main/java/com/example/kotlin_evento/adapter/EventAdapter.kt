package com.example.kotlin_evento.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_evento.R
import com.example.kotlin_evento.models.Event
import com.example.kotlin_evento.utils.Convert
import com.example.kotlin_evento.utils.OnClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.evento_item.view.*


class EventAdapter (onClickListener: OnClickListener) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    var listEvents = ArrayList<Event>()
    private var onClick : OnClickListener = onClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.evento_item, parent, false)
        return ViewHolder(inflater, this.onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listEvents[position])
    }

    override fun getItemCount(): Int {
        return listEvents.size
    }

    fun getItem(position: Int): Event {
        return listEvents[position]
    }

    class ViewHolder(view : View, onClickListener: OnClickListener) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.title_evento
        private val date: TextView = view.data_evento
        private val price: TextView = view.preco_evento
        private val image: ImageView = view.image_evento

        init {
            view.setOnClickListener {
                onClickListener.onClickItemClicked(adapterPosition)
            }
        }

        fun bind(data : Event) {
            Log.d("EventoAdapter", "ViewHolder")
            title.text = data.title
            date.text = Convert.getTimestampToDate(data.date)
            price.text = Convert.getPriceReal(data.price)

            Picasso.get()
                .load(data.image)
                .placeholder(R.drawable.ic_picasso_placeholder)
                .error(R.drawable.ic_picasso_not_suported)
                .into(image)

        }
    }

}