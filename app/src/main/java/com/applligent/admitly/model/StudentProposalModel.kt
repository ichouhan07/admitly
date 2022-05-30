package com.applligent.admitly.model

data class StudentProposalModel(
    val proposalId: Int,
    val projectId: Int,
    val studentId: Int,
    val counsellorId: Int,
    val priceType: Int,
    val price: Int,
    val coverLetter: String,
    val status: Int,
    val created: String,
    val updated: String,
    val countryId: Int,
    val year: Int,
    val major: String,
    val serviceId: Int,
    val counsellor: Counsellor
    )
   data class Counsellor(
    val name: String,
    val profileUrl: String,
    val experience: String,
    val averageRating: Int
    )