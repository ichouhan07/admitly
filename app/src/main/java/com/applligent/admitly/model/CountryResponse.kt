package com.applligent.admitly.model

data class CountryResponse(var success:Boolean,
                        var statusCode:Int,
                        var type:Int,
                        var message:String,
                        var data:List<CountryModel>?)
data class CountryModel(
    var id: Int?,
    var name:String?,
    var iso3:String?)
