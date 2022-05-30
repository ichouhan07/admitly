package com.applligent.admitly.model

data class CounselorExploreModel(
    val projectId: Int,
    val studentId: Int,
    val countryId: Int,
    val year: Int,
    val major: String,
    val serviceId: Int,
    val status: Int,
    val created: String,
    val countryName: String,
    val serviceName: String,
)