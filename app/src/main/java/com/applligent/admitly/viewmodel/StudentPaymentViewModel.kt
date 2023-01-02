package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.ui.comman.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentPaymentViewModel(val repository: Repository) : ViewModel() {
    val addCardCallback = MutableLiveData<ApiCallback>()

    fun getAddCard(map:HashMap<String,Any>, token: String){
        addCardCallback.value = ApiCallback.Loading(true)
        val response = repository.studentAddCard(map,token)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                //  ApiCallback.loginCallback.value = response
                addCardCallback.value = ApiCallback.Loading(false)
                addCardCallback.value = ApiCallback.Success(response.body()!!)
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                addCardCallback.value = ApiCallback.Loading(false)
                addCardCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }
}
class StudentPaymentViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StudentPaymentViewModel::class.java)) {
            StudentPaymentViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}