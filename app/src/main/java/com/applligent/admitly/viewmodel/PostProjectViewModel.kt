package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.ui.comman.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostProjectViewModel(val repository: Repository): ViewModel() {

    val projectCallback = MutableLiveData<ApiCallback>()
    val countryCallback = MutableLiveData<ApiCallback>()

    fun addProject(map:HashMap<String,Any>,token:String){
        projectCallback.value = ApiCallback.Loading(true)
        val response = repository.addProject(map,token)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                projectCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    projectCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    projectCallback.value = ApiCallback.Error(response.toString())
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                projectCallback.value = ApiCallback.Loading(false)
                projectCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }

    fun getAllCountry(){
        countryCallback.value = ApiCallback.Loading(true)
        val response = repository.getAllCountries()
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                countryCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    countryCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    countryCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                countryCallback.value = ApiCallback.Loading(false)
                countryCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }

}
class PostProjectViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PostProjectViewModel::class.java)) {
            PostProjectViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}