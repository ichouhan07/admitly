package com.applligent.admitly

import android.content.Context
import com.applligent.admitly.getCounty.Country
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiInterface {
    @GET("get/countries")
    fun getCountries(): Call<Any>


    companion object {
        private const val BASE_URL = "https://applligent.com/projects/admitly_backend/"

        var retrofit: Retrofit? = null
        private val logging = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        fun getInstance(context: Context) : Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(OkHttpClient.Builder().addInterceptor(logging).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}