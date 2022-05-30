package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.ui.comman.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentDBViewModel(val repository: Repository): ViewModel() {

    val exploreCounselorCallback = MutableLiveData<ApiCallback>()
    val countryCallback = MutableLiveData<ApiCallback>()
    val serviceCallback = MutableLiveData<ApiCallback>()
    val projectCallback = MutableLiveData<ApiCallback>()
    val proposalCallback = MutableLiveData<ApiCallback>()
    val hireCounselorCallback = MutableLiveData<ApiCallback>()

    fun getExploreCounselor(map:HashMap<String,Any>){
        exploreCounselorCallback.value = ApiCallback.Loading(true)
        val response = repository.getCounselor(map)
        response.enqueue(object : Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                exploreCounselorCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    exploreCounselorCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    exploreCounselorCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                exploreCounselorCallback.value = ApiCallback.Loading(false)
                exploreCounselorCallback.value = ApiCallback.Error(t.message.toString())
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
    fun getStudentProject(map:HashMap<String,Any>,token:String){
        projectCallback.value = ApiCallback.Loading(true)
        val response = repository.getStudentProject(map,token)
        response.enqueue(object : Callback<Any>{
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
    fun getStudentProposal(map:HashMap<String,Any>,token:String){
        proposalCallback.value = ApiCallback.Loading(true)
        val response = repository.getStudentProposal(map,token)
        response.enqueue(object : Callback<Any>{
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
    fun getHireCounselor(map:HashMap<String,Any>,token:String){
        hireCounselorCallback.value = ApiCallback.Loading(true)
        val response = repository.counsellorHire(map,token)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                hireCounselorCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    hireCounselorCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    hireCounselorCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                hireCounselorCallback.value = ApiCallback.Loading(false)
                hireCounselorCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }
}
class StudentDBViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StudentDBViewModel::class.java)) {
            StudentDBViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}