package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.network.ApiCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(val repository: Repository): ViewModel() {

    val signupCallback = MutableLiveData<ApiCallback>()
    val countryCallback = MutableLiveData<ApiCallback>()
    val profilePicCallback = MutableLiveData<ApiCallback>()

    fun userSignup(map:HashMap<String,Any>){
        signupCallback.value = ApiCallback.Loading(true)
        val response = repository.userSignup(map)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                signupCallback.value = ApiCallback.Loading(false)
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
    fun getProfilePic(map:HashMap<String,Any>,token: String){
        profilePicCallback.value = ApiCallback.Loading(true)
        val response = repository.userProfilePicture(map,token)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                profilePicCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    profilePicCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    profilePicCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                profilePicCallback.value = ApiCallback.Loading(false)
                profilePicCallback.value = ApiCallback.Error(t.message.toString())
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