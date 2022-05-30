package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.network.ApiCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePasswordViewModel(val repository: Repository) : ViewModel() {

    val updatePasswordCallback = MutableLiveData<ApiCallback>()

    fun resetPassword(map:HashMap<String,Any>){
        updatePasswordCallback.value = ApiCallback.Loading(true)
        val response = repository.resetPassword(map)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                //  ApiCallback.loginCallback.value = response
                updatePasswordCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    updatePasswordCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    updatePasswordCallback.value = ApiCallback.Error(response.toString())
                }

            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                updatePasswordCallback.value = ApiCallback.Loading(false)
                updatePasswordCallback.value = ApiCallback.Error(t.message.toString())
            }
        })





    }

}

class UpdatePasswordViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UpdatePasswordViewModel::class.java)) {
            UpdatePasswordViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}