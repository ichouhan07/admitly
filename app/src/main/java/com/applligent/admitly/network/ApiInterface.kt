package com.applligent.admitly.network

import com.applligent.admitly.utils.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    @GET(GET_COUNTRY_API)
    fun getCountries(): Call<Any>

    @GET(GET_SERVICES_API)
    fun getServices(): Call<Any>

    @POST(USER_LOGIN_API)
    @JvmSuppressWildcards
    fun userLogin(@Body body: Map<String, Any>): Call<Any>

    @POST(USER_SIGNUP_API)
    @JvmSuppressWildcards
    fun userSignup(@Body body: Map<String, Any>): Call<Any>

    @POST(FORGOT_PASS_API)
    @JvmSuppressWildcards
    fun forgotPassword(@Body body: Map<String, Any>): Call<Any>

    @POST(VERIFY_OTP_API)
    @JvmSuppressWildcards
    fun verifyOtp(@Body body: Map<String, Any>): Call<Any>

    @POST(RESET_PASSWORD_API)
    @JvmSuppressWildcards
    fun resetPassword(@Body body: Map<String, Any>): Call<Any>

    @POST(STUDENT_ADD_PROJECT_API)
    @JvmSuppressWildcards
    fun studentAddProject(@Body body: Map<String, Any>, @Header(AUTHORIZATION) token: String): Call<Any>

    @POST(COUNSELLOR_ADD_SERVICE_API)
    @JvmSuppressWildcards
    fun counselorAddService(@Body body: Map<String, Any>, @Header(AUTHORIZATION) token: String): Call<Any>

    @POST(GET_COUNSELOR_API)
    @JvmSuppressWildcards
    fun getCounselor(@Body body: Map<String, Any>): Call<Any>

    @POST(STUDENT_ALL_PROJECTS_API)
    @JvmSuppressWildcards
    fun studentALLProject(@Body body: Map<String, Any>, @Header(AUTHORIZATION) token: String): Call<Any>

    @POST(STUDENT_PROPOSALS_ALL_API)
    @JvmSuppressWildcards
    fun studentProposalAll(@Body body: Map<String, Any>, @Header(AUTHORIZATION) token: String): Call<Any>

    @POST(COUNSELLOR_HIRE_API)
    @JvmSuppressWildcards
    fun counsellorHire(@Body body: Map<String, Any>, @Header(AUTHORIZATION) token: String): Call<Any>

    @POST(USER_PROJECTS_API)
    @JvmSuppressWildcards
    fun userProjects(@Body body: Map<String, Any>): Call<Any>

    @GET(COUNSELLOR_PROPOSALS_ALL_API)
    fun getCounselorProposalAll(@Header(AUTHORIZATION) token: String): Call<Any>

    @POST(STUDENT_PROPOSAL_API)
    @JvmSuppressWildcards
    fun studentProposal(@Body body: Map<String, Any>,
                       @Header(AUTHORIZATION) token: String): Call<Any>

    @POST(STUDENT_ADD_RATING_API)
    @JvmSuppressWildcards
    fun studentAddRating(@Body body: Map<String, Any>,@Header(AUTHORIZATION) token: String): Call<Any>

    @POST(USER_PROFILE_PICTURE_API)
    @JvmSuppressWildcards
    fun userProfilePicture(@Body body: Map<String, Any>,@Header(AUTHORIZATION) token: String): Call<Any>

    @POST(STUDENT_ADD_CARD_API)
    @JvmSuppressWildcards
    fun studentAddCard(@Body body: Map<String, Any>,@Header(AUTHORIZATION) token: String): Call<Any>

    @POST(COUNSELLOR_ADD_ACCOUNT_API)
    @JvmSuppressWildcards
    fun counsellorAddAccount(@Body body: Map<String, Any>, @Header(AUTHORIZATION) token: String): Call<Any>
}