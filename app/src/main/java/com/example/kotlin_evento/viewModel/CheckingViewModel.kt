package com.example.kotlin_evento.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin_evento.request.CheckinApi
import com.example.kotlin_evento.request.ServiceConnect
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class CheckingViewModel : ViewModel(){

    private var response: MutableLiveData<ResponseBody> = MutableLiveData()

    fun getCheckingObserver() : MutableLiveData<ResponseBody> {
        return  response
    }

    fun makeApiCall(id : String, nome : String, email : String){
        val serviceConnect = ServiceConnect.getInstance().create(CheckinApi::class.java)
        serviceConnect.postCheckin(id, nome, email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCheckingListObserver())

    }

    private fun getCheckingListObserver() : Observer<ResponseBody>  {
        return  object : Observer<ResponseBody>{
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: ResponseBody) {
                response.postValue(t)
            }

            override fun onError(e: Throwable) {
                response.postValue(null)
                Log.d("RESPOSTA", e.message.toString())
            }

            override fun onComplete() {
            }

        }
    }

}