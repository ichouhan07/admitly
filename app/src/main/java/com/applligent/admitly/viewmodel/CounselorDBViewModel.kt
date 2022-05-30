package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.ui.comman.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounselorDBViewModel(val repository: Repository): ViewModel() {
    val exploreCallback = MutableLiveData<ApiCallback>()
    val countryCallback = MutableLiveData<ApiCallback>()
    val serviceCallback = MutableLiveData<ApiCallback>()
    val proposalCallback = MutableLiveData<ApiCallback>()
    val studentProposalCallback = MutableLiveData<ApiCallback>()


    fun getUserProjects(map:HashMap<String,Any>){
        exploreCallback.value = ApiCallback.Loading(true)
        val response = repository.getUserProjects(map)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                exploreCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    exploreCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    exploreCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                exploreCallback.value = ApiCallback.Loading(false)
                exploreCallback.value = ApiCallback.Error(t.message.toString())
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
    fun getProposalAll(token:String){
        proposalCallback.value = ApiCallback.Loading(true)
        val response = repository.getCounselorProposalAll(token)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                proposalCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    proposalCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    proposalCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                proposalCallback.value = ApiCallback.Loading(false)
                proposalCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }
    fun getStudentProposal(map:HashMap<String,Any>,token: String){
        studentProposalCallback.value = ApiCallback.Loading(true)
        val response = repository.studentProposal(map,token)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                studentProposalCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    studentProposalCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    studentProposalCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                studentProposalCallback.value = ApiCallback.Loading(false)
                studentProposalCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }
}
class CounselorDBViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CounselorDBViewModel::class.java)) {
            CounselorDBViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}