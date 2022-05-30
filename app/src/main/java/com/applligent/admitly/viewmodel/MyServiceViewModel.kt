package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.ui.comman.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyServiceViewModel (val repository: Repository): ViewModel() {

    val myServiceCallback = MutableLiveData<ApiCallback>()
    val serviceCallback = MutableLiveData<ApiCallback>()

    fun addService(map:HashMap<String,Any>,token:String){
        myServiceCallback.value = ApiCallback.Loading(true)
        val response = repository.addService(map,token)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                myServiceCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    myServiceCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    myServiceCallback.value = ApiCallback.Error(response.toString())
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                myServiceCallback.value = ApiCallback.Loading(false)
                myServiceCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }

    fun getAllService(){
        serviceCallback.value = ApiCallback.Loading(true)
        val response = repository.getAllServices()
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                serviceCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    serviceCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    serviceCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                serviceCallback.value = ApiCallback.Loading(false)
                serviceCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }
}
class MyServiceViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MyServiceViewModel::class.java)) {
            MyServiceViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}