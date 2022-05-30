package com.applligent.admitly.model

data class StudentExploreModel (
        val userId: Int,
        val name: String,
        val email: String,
        val userType: Int,
        val socialId: String,
        val socialType: String,
        val countryId: Int,
        val countryName: String,
        val status: Int,
        val occupation: String,
        val linkedInURL: String,
        val profileURL: String,
        val services: List<Service>,
        val averageRating: Double,
        val rating: Rating
    )
    data class Rating (
        val competency: Double,
        val professionalism: Double,
        val responsiveness: Double
    )
    data class Service (
        val serviceName: String,
        val userServiceId: Int,
        val serviceId: Int,
        val counsellorId: Int,
        val experience: Double,
        val priceType: Int,
        val price: Int
    )
