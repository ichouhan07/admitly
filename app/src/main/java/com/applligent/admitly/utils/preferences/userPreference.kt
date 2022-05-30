package com.applligent.admitly.utils.preferences

import android.content.Context

private const val FILE_NAME = "user_pref"

fun Context.getUserId() : Int {
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt("user_id", -1) ?: -1
}

fun Context.setUserId(userId: Int){
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("user_id", userId).apply()
}

fun Context.getToken() : String {
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString("token", "") ?: ""
}
fun Context.setToken(token: String){
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("token", token).apply()
}

fun Context.getLoginStatus() : Boolean {
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isLogin", false)
}
fun Context.setLoginStatus(isLogin: Boolean){
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isLogin", isLogin).apply()
}
fun Context.getUserType() : Int {
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt("userType", -1)
}
fun Context.setUserType(userType: Int){
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("userType", userType).apply()
}

fun Context.getAccountType() : Int {
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt("accountType", -1)
}
fun Context.setAccountType(accountType: Int){
    val sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("accountType", accountType).apply()
}