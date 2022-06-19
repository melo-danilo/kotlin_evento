package com.example.kotlin_evento.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin_evento.models.Evento
import com.example.kotlin_evento.request.EventoApi
import com.example.kotlin_evento.request.ServiceConnect
import com.example.kotlin_evento.utils.Connection
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventoViewModel: ViewModel() {

    var eventoList: MutableLiveData<List<Evento>> = MutableLiveData()

    fun getEventoListObserver() : MutableLiveData<List<Evento>> {
        return  eventoList
    }

    fun makeApiCall(){
        Log.d("EventoViewModel", "makeApiCall")
        val serviceConnect = ServiceConnect.getInstance().create(EventoApi::class.java)
        serviceConnect.getEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getEventListObserver())

    }

    private fun getEventListObserver() : Observer<List<Evento>> {
        return  object : Observer<List<Evento>>{
            override fun onComplete() {
                Log.d("getEventListObserver", "onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d("getEventListObserver", "onSubscribe")
                //d.dispose()
            }

            override fun onNext(t: List<Evento>) {
                Log.d("getEventListObserver", "onNext")
                eventoList.postValue(t)
            }

            override fun onError(e: Throwable) {
                Log.d("getEventListObserver", "onError" + e.toString())
                eventoList.postValue(null)
            }

        }
    }
}