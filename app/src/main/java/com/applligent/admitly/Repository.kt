package com.applligent.admitly

class Repository (private val apiInterface: ApiInterface) {
    fun getAllCountries() = apiInterface.getCountries()
}