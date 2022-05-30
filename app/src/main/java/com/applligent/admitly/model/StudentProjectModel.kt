package com.applligent.admitly.model

data class StudentProjectModel(
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
    val dreamSchool: List<DreamSchool>
    )
   data class DreamSchool(
       val dreamSchoolId: Int,
       val projectId: Int,
       val studentId: Int,
       val title: String
   )