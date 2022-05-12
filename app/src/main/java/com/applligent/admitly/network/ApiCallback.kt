package com.applligent.admitly.network

sealed class ApiCallback{
    class Success(val data:Any) : ApiCallback()
    class Exception(val exception:String) : ApiCallback()
    class Error(val error:String) : ApiCallback()
    class Loading(val isLoading:Boolean) : ApiCallback()

}

