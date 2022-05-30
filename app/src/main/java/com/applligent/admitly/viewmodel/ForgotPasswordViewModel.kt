package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.network.ApiCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel(val repository: Repository) : ViewModel() {

    val fPasswordCallback = MutableLiveData<ApiCallback>()
    val verifyOtpCallback = MutableLiveData<ApiCallback>()

    fun forgotPassword(map:HashMap<String,Any>){
        fPasswordCallback.value = ApiCallback.Loading(true)
        val response = repository.forgotPassword(map)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                //  ApiCallback.loginCallback.value = response
                fPasswordCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    fPasswordCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    fPasswordCallback.value = ApiCallback.Error(response.toString())
                }

            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                fPasswordCallback.value = ApiCallback.Loading(false)
                fPasswordCallback.value = ApiCallback.Error(t.message.toString())
            }
        })


    }


    fun verifyOtp(map:HashMap<String,Any>){
        verifyOtpCallback.value = ApiCallback.Loading(true)
        val response = repository.verifyOtp(map)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                //  ApiCallback.loginCallback.value = response
                verifyOtpCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    verifyOtpCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    verifyOtpCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                verifyOtpCallback.value = ApiCallback.Loading(false)
                verifyOtpCallback.value = ApiCallback.Error(t.message.toString())
            }
        })


    }

}

class ForgotPasswordViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            ForgotPasswordViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}