package com.applligent.admitly.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.Repository
import com.applligent.admitly.network.ApiCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(val repository: Repository): ViewModel() {

    val signupCallback = MutableLiveData<ApiCallback>()

    fun userSignup(map:HashMap<String,Any>){
        signupCallback.value = ApiCallback.Loading(true)
        val response = repository.userSignup(map)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                signupCallback.value = ApiCallback.Loading(false)
                 System.out.println("MY_RESPONSE is "+response.toString())
                System.out.println("MY_RESPONSE is body "+response.body().toString())
                if (response.body()!=null){
                    signupCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    signupCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                signupCallback.value = ApiCallback.Loading(false)
                signupCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }
}

class SignupViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            SignupViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}