package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.network.ApiCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(val repository: Repository) : ViewModel() {

    val loginCallback = MutableLiveData<ApiCallback>()

    fun userLogin(map:HashMap<String,Any>){
        loginCallback.value = ApiCallback.Loading(true)
        val response = repository.userLogin(map)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
              //  ApiCallback.loginCallback.value = response
                loginCallback.value = ApiCallback.Loading(false)
                loginCallback.value = ApiCallback.Success(response.body()!!)
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                loginCallback.value = ApiCallback.Loading(false)
                loginCallback.value = ApiCallback.Error(t.message.toString())
            }
        })


    }

}

class LoginViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}