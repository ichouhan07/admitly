package com.applligent.admitly.ui.comman

import com.applligent.admitly.network.ApiInterface

class Repository (private val apiInterface: ApiInterface) {

    fun getAllCountries() = apiInterface.getCountries()

    fun getAllServices() = apiInterface.getServices()

    fun userLogin(map:HashMap<String,Any>) = apiInterface.userLogin(map)

    fun userSignup(map:HashMap<String,Any>) = apiInterface.userSignup(map)

    fun forgotPassword(map:HashMap<String,Any>) = apiInterface.forgotPassword(map)

    fun verifyOtp(map:HashMap<String,Any>) = apiInterface.verifyOtp(map)

    fun resetPassword(map:HashMap<String,Any>) = apiInterface.resetPassword(map)

    fun addProject(map: HashMap<String, Any>,token:String) = apiInterface.studentAddProject(map,token)

    fun addService(map: HashMap<String, Any>,token:String) = apiInterface.counselorAddService(map,token)

    fun getCounselor(map:HashMap<String,Any>) = apiInterface.getCounselor(map)

    fun getStudentProject(map: HashMap<String, Any>,token:String) = apiInterface.studentALLProject(map,token)

    fun getStudentProposal(map: HashMap<String, Any>,token:String) = apiInterface.studentProposalAll(map,token)

    fun counsellorHire(map: HashMap<String, Any>,token:String) = apiInterface.counsellorHire(map,token)

    fun getUserProjects(map:HashMap<String,Any>) = apiInterface.userProjects(map)

    fun getCounselorProposalAll(token:String) = apiInterface.getCounselorProposalAll(token)

    fun studentProposal(map: HashMap<String, Any>,token:String) = apiInterface.studentProposal(map,token)

    fun studentAddRating(map: HashMap<String, Any>,token:String) = apiInterface.studentAddRating(map,token)


}