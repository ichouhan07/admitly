package com.applligent.admitly.model

data class CounselorProposalModel (
    val proposalId: Int,
    val projectId: Int,
    val studentId: Int,
    val counsellorId: Int,
    val priceType: Int,
    val price: Int,
    val coverLetter: String,
    val status: Int,
    val created: String,
    val updated: String
    )