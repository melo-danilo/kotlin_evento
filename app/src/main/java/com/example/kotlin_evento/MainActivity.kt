package com.example.kotlin_evento

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.kotlin_evento.adapter.EventoAdapter
import com.example.kotlin_evento.models.Evento
import com.example.kotlin_evento.request.EventoApi
import com.example.kotlin_evento.request.ServiceConnect
import com.example.kotlin_evento.utils.OnClickListener
import com.example.kotlin_evento.viewModel.EventoViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.evento_detalhado.view.*
import kotlinx.android.synthetic.main.evento_detalhado.view.data_evento
import kotlinx.android.synthetic.main.evento_detalhado.view.image_evento
import kotlinx.android.synthetic.main.evento_detalhado.view.preco_evento
import kotlinx.android.synthetic.main.evento_detalhado.view.title_evento
import kotlinx.android.synthetic.main.evento_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnClickListener {

    lateinit var viewModel : EventoViewModel
    lateinit var eventoAdapter: EventoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "onCreate")


        initRecyclerView()
        loadApiData()
    }

    fun initRecyclerView() {
        recyclerView.apply {
            val decoration = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(decoration)
            layoutManager = LinearLayoutManager(this@MainActivity)
            eventoAdapter = EventoAdapter(this@MainActivity)
            adapter = eventoAdapter
        }
    }

    fun loadApiData () {
        viewModel = ViewModelProvider(this).get(EventoViewModel::class.java)
        viewModel.getEventoListObserver().observe(this, Observer<List<Evento>>{
            Log.d("loadApiData", "sucesso")

            if(it != null) {
                eventoAdapter.listEventos = it as ArrayList<Evento>
                eventoAdapter.notifyDataSetChanged()
            }
            else {
                Log.d("loadApiData", "falha")

                Toast.makeText(this, "Error in fetching data", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.makeApiCall()
    }

    override fun onClickItemClicked(position: Int) {
        chamarAlert(position)
    }

    fun chamarAlert(position: Int) {

        val view = View.inflate(this@MainActivity, R.layout.evento_detalhado, null)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(view)

        val dialog = builder.create()

        val eventoId = eventoAdapter.getItem(position).id

        val serviceConnect = ServiceConnect.getInstance().create(EventoApi::class.java)
        serviceConnect.getEvent(eventoId).enqueue(object : Callback<Evento>{
            override fun onResponse(call: Call<Evento>, response: Response<Evento>) {
                if(response.isSuccessful){
                    val evento : Evento = response.body()!!

                    view.title_evento.text = evento.title
                    view.data_evento.text = evento.date.toString()
                    view.preco_evento.text = "R$ ${evento.price}"
                    view.descricao_evento.text = evento.description

                    Picasso.get()
                        .load(evento.image)
                        .placeholder(R.drawable.ic_picasso_placeholder)
                        .error(R.drawable.ic_picasso_not_suported)
                        .into(view.image_evento)

                    view.close_evento.setOnClickListener(View.OnClickListener {
                        dialog.cancel()
                    })

                    view.button_local.setOnClickListener(View.OnClickListener {
                        val uri: String = java.lang.String.format(
                            Locale.US,
                            "geo:0,0,?q=%f,%f",
                            evento.latitude,
                            evento.longitude,
                        )
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        this@MainActivity.startActivity(intent)
                    })

                    view.button_checkin.setOnClickListener(View.OnClickListener {
                        dialog.cancel()
                        val intent = Intent(this@MainActivity, CheckinActivity::class.java)
                        intent.putExtra("id_evento", evento.id)
                        intent.putExtra("url_image", evento.image)
                        intent.putExtra("title_evento", evento.title)
                        this@MainActivity.startActivity(intent)
                    })
                }
            }

            override fun onFailure(call: Call<Evento>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }



}