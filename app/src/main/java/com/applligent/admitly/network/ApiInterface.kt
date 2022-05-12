package com.applligent.admitly.network

import com.applligent.admitly.utils.GET_COUNTRY_API
import com.applligent.admitly.utils.USER_LOGIN_API
import com.applligent.admitly.utils.USER_SIGNUP_API
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @GET(GET_COUNTRY_API)
    fun getCountries(): Call<Any>

    @POST(USER_LOGIN_API)
    @JvmSuppressWildcards
    fun userLogin(@Body body: Map<String, Any>): Call<Any>

    @POST(USER_SIGNUP_API)
    @JvmSuppressWildcards
    fun userSignup(@Body body: Map<String, Any>): Call<Any>




}