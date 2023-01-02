package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.ui.comman.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounselorPaymentViewModel(val repository: Repository) : ViewModel() {
    val addBankCallBack = MutableLiveData<ApiCallback>()

    fun getAddAccount(map: HashMap<String, Any>, token: String){
        addBankCallBack.value = ApiCallback.Loading(true)
        val response = repository.counsellorAddAccount(map,token)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                addBankCallBack.value = ApiCallback.Loading(false)
                addBankCallBack.value = ApiCallback.Success(response.body()!!)
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                addBankCallBack.value = ApiCallback.Loading(false)
                addBankCallBack.value = ApiCallback.Error(t.message.toString())
            }
        })
    }
}
class CounselorPaymentViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CounselorPaymentViewModel::class.java)) {
            CounselorPaymentViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
