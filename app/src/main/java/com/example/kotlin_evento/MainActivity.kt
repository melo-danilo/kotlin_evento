package com.example.kotlin_evento

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.kotlin_evento.adapter.EventAdapter
import com.example.kotlin_evento.models.Event
import com.example.kotlin_evento.utils.Connection
import com.example.kotlin_evento.utils.Convert
import com.example.kotlin_evento.utils.OnClickListener
import com.example.kotlin_evento.viewModel.EventViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.evento_detalhado.view.*
import kotlinx.android.synthetic.main.evento_detalhado.view.data_evento
import kotlinx.android.synthetic.main.evento_detalhado.view.image_evento
import kotlinx.android.synthetic.main.evento_detalhado.view.preco_evento
import kotlinx.android.synthetic.main.evento_detalhado.view.title_evento
import kotlinx.android.synthetic.main.evento_item.view.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var viewModel : EventViewModel
    private lateinit var viewAlertDialog: View
    private lateinit var eventAdapter: EventAdapter
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initRecyclerView()
        if(Connection.isOnline(this)){
            loadApiData()
        }else{
            carregando.visibility = View.GONE
            not_wifi.visibility = View.VISIBLE
            Toast.makeText(this, "No Connected", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initRecyclerView() {
        recyclerView.apply {
            val decoration = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(decoration)
            layoutManager = LinearLayoutManager(this@MainActivity)
            eventAdapter = EventAdapter(this@MainActivity)
            adapter = eventAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadApiData () {
        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        viewModel.getEvents().observe(this) {

            if (it != null) {
                carregando.visibility = View.GONE
                eventAdapter.listEvents = it as ArrayList<Event>
                eventAdapter.notifyDataSetChanged()
            } else {
                carregando.visibility = View.GONE
                error_data.visibility = View.VISIBLE
                Toast.makeText(this, "Error in fetching data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.makeApiCall()
    }

    override fun onClickItemClicked(position: Int) {
        showAlert(position)
    }

    private fun showAlert(position: Int) {

        viewAlertDialog = View.inflate(this@MainActivity, R.layout.evento_detalhado, null)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(viewAlertDialog)

        dialog = builder.create()

        val eventId = eventAdapter.getItem(position).id
        
        viewModel.getEvent().observe(this) {
            if (it != null) {

                val id: Int = it.id
                val title: String = it.title
                val latitude: Float = it.latitude
                val longitude: Float = it.longitude
                val image: String = it.image
                val date: String = Convert.getTimestampToDate(it.date)
                val price = Convert.getPriceReal(it.price)
                val description: String = it.description

                viewAlertDialog.title_evento.text = title
                viewAlertDialog.data_evento.text = date.toString()
                viewAlertDialog.preco_evento.text = price
                viewAlertDialog.descricao_evento.text = description

                Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.ic_picasso_placeholder)
                    .error(R.drawable.ic_picasso_not_suported)
                    .into(viewAlertDialog.image_evento)

                viewAlertDialog.button_local.setOnClickListener {
                    val uri: String = java.lang.String.format(
                        Locale.US,
                        "geo:0,0,?q=%f,%f",
                        latitude,
                        longitude,
                    )
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    this@MainActivity.startActivity(intent)
                }

                viewAlertDialog.button_checkin.setOnClickListener {
                    dialog.dismiss()

                    val intent = Intent(this@MainActivity, CheckinActivity::class.java)
                    intent.putExtra("id_event", id)
                    intent.putExtra("url_image", image)
                    intent.putExtra("title_event", title)
                    this@MainActivity.startActivity(intent)

                }
            }
        }

        viewModel.makeApiCallItem(eventId)
        
        viewAlertDialog.close_evento.setOnClickListener {
            dialog.cancel()
        }

        viewAlertDialog.image_evento.setImageResource(R.drawable.ic_picasso_placeholder)
        viewAlertDialog.title_evento.text = ""
        viewAlertDialog.data_evento.text = ""
        viewAlertDialog.preco_evento.text = ""
        viewAlertDialog.descricao_evento.text = ""

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}