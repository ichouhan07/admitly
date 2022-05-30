package com.applligent.admitly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.ui.comman.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewCounselorViewModel(val repository: Repository): ViewModel() {
    val reviewCallback = MutableLiveData<ApiCallback>()

    fun getReviewCounselor(map:HashMap<String,Any>,token: String){
        reviewCallback.value = ApiCallback.Loading(true)
        val response = repository.studentAddRating(map,token)
        response.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                reviewCallback.value = ApiCallback.Loading(false)
                if (response.body()!=null){
                    reviewCallback.value = ApiCallback.Success(response.body()!!)
                }else{
                    reviewCallback.value = ApiCallback.Error(response.toString())
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                ApiCallback.Loading(false)
                reviewCallback.value = ApiCallback.Loading(false)
                reviewCallback.value = ApiCallback.Error(t.message.toString())
            }
        })
    }

}
class  ReviewCounselorViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReviewCounselorViewModel::class.java)) {
            ReviewCounselorViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}