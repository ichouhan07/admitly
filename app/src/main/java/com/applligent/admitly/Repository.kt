package com.applligent.admitly

import com.applligent.admitly.network.ApiInterface

class Repository (private val apiInterface: ApiInterface) {

    fun getAllCountries() = apiInterface.getCountries()

    fun userLogin(map:HashMap<String,Any>) = apiInterface.userLogin(map)

    fun userSignup(map:HashMap<String,Any>) = apiInterface.userSignup(map)


}