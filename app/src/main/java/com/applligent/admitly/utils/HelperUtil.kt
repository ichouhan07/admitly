package com.applligent.admitly.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.applligent.admitly.BuildConfig

fun Any.log(text: String) {
    if (BuildConfig.DEBUG) Log.d("${this::class.java.simpleName} admitly_debug", text)
}
fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}
fun Fragment.toast(text: String) {
    requireActivity().toast(text)
}