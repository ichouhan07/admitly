package com.applligent.admitly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.getCounty.Country
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInViewModel (private val repository: Repository) : ViewModel() {
  /* val countryList = MutableLiveData<List<Country>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllCountries(){
        val response = repository.getAllCountries()
        response.enqueue(object : Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {

            }
            override fun onFailure(call: Call<Any>, t: Throwable) {

            }
        })
    }*/
}
class SignInViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            SignInViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

