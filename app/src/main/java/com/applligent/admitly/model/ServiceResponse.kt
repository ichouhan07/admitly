package com.applligent.admitly.model

data class ServiceResponse(var success:Boolean,
                           var statusCode:Int,
                           var type:Int,
                           var message:String,
                           var data:List<ServiceModel>?)
data class ServiceModel(
    var serviceId: Int?,
    var title:String?,
    var status:Int?)