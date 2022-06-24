package com.example.kotlin_evento.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin_evento.models.Event
import com.example.kotlin_evento.request.EventApi
import com.example.kotlin_evento.request.ServiceConnect
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventViewModel: ViewModel() {

    var eventList: MutableLiveData<List<Event>> = MutableLiveData()
    private var event: MutableLiveData<Event> = MutableLiveData()

    fun getEvents() : MutableLiveData<List<Event>> {
        return  eventList
    }

    fun getEvent() : MutableLiveData<Event> {
        return  event
    }

    fun makeApiCall(){
        val serviceConnect = ServiceConnect.getInstance().create(EventApi::class.java)
        serviceConnect.getEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getEventListObserver())

    }

    fun makeApiCallItem(id : Int){
        val serviceConnect = ServiceConnect.getInstance().create(EventApi::class.java)
        serviceConnect.getEvent(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this.getEventObserver())

    }

    private fun getEventListObserver() : Observer<List<Event>> {
        return  object : Observer<List<Event>>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                //d.dispose()
            }

            override fun onNext(t: List<Event>) {
                eventList.postValue(t)
            }

            override fun onError(e: Throwable) {
                eventList.postValue(null)
            }

        }
    }

    private fun getEventObserver() : Observer<Event> {
        return  object : Observer<Event>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                //d.dispose()
            }

            override fun onNext(t: Event) {
                event.postValue(t)
            }

            override fun onError(e: Throwable) {
                event.postValue(null)
            }

        }
    }
}