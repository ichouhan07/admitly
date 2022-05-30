package com.applligent.admitly.ui.comman

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.databinding.ActivityForgotPasswordBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.counselor.MyServiceActivity
import com.applligent.admitly.ui.student.PostProjectActivity
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.viewmodel.ForgotPasswordViewModel
import com.applligent.admitly.viewmodel.ForgotPasswordViewModelFactory
import com.applligent.admitly.viewmodel.LoginViewModel
import com.applligent.admitly.viewmodel.LoginViewModelFactory
import com.google.gson.Gson
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding

    lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    private lateinit var progressDialog: ProgressDialog
    private var isOtp:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        forgotPasswordViewModel = ViewModelProvider(
            this,
            ForgotPasswordViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(ForgotPasswordViewModel::class.java)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")

        setListeners()
        setObserVer()

    }
    private fun setListeners(){
        binding.forgetBack.setOnClickListener { onBackPressed() }
        binding.forgotPassSubmitBtn.setOnClickListener {

            if(binding.otpLayout.visibility == View.VISIBLE){
                if (isValidOtpEmail() == true){
                    progressDialog.show()
                    val emailMap = HashMap<String, Any>()
                    emailMap.put("email",binding.etForgotPassEmail.text.toString().trim())
                    emailMap.put("otp",binding.otpEt.text.toString().trim())
                    forgotPasswordViewModel.verifyOtp(emailMap)
                }
            }else{
                if (isValidSignUpDetails() == true){
                    progressDialog.show()
                    val emailMap = HashMap<String, Any>()
                    emailMap.put("email",binding.etForgotPassEmail.text.toString().trim())
                    forgotPasswordViewModel.forgotPassword(emailMap)
                }
            }
        }
    }


    private fun isValidSignUpDetails(): Boolean? {
        return if (binding.etForgotPassEmail.text.toString().trim().isEmpty()) {
            binding.etForgotPassEmail.requestFocus()
            binding.etForgotPassEmail.error = "Please enter email !"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etForgotPassEmail.text.toString())
                .matches()
        ) {
            binding.etForgotPassEmail.requestFocus()
            binding.etForgotPassEmail.error = "Please enter valid email !"
            false
        }else {
            true
        }
    }



    private fun isValidOtpEmail(): Boolean? {
        return if (binding.etForgotPassEmail.text.toString().trim().isEmpty()) {
            binding.etForgotPassEmail.requestFocus()
            binding.etForgotPassEmail.error = "Please enter email !"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etForgotPassEmail.text.toString())
                .matches()
        ) {
            binding.etForgotPassEmail.requestFocus()
            binding.etForgotPassEmail.error = "Please enter valid email !"
            false
        }else if(binding.otpEt.text.toString().trim().isEmpty()){
            binding.otpEt.requestFocus()
            binding.otpEt.error = "Please enter otp !"
            false
        }else{
            true
        }
    }

    private fun setObserVer(){

        forgotPasswordViewModel.fPasswordCallback.observe(this
        ) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    isOtp = true
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    // System.out.println("LOGIN_RESPONSE_IS "+mainObject.toString())
                    if (mainObject.getBoolean("success")) {
                        binding.otpLayout.visibility = View.VISIBLE
                        Comman.showLongToast(this, "Please check your email\nwe have sent an OTP on your email")
                    } else {
                        Comman.showLongToast(this, mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    Comman.showLongToast(this, response.error.toString())
                }
                is ApiCallback.Loading -> {
                    if (!response.isLoading) {
                        progressDialog.dismiss()
                    }
                }
            }
        }


        forgotPasswordViewModel.verifyOtpCallback.observe(this
        ) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    isOtp = true
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    // System.out.println("LOGIN_RESPONSE_IS "+mainObject.toString())
                    if (mainObject.getBoolean("success")) {
                        binding.otpLayout.visibility = View.GONE
                        Comman.showLongToast(this, "OTP Verified")
                        val intent = Intent(this,UpdatePasswordActivity::class.java)
                        intent.putExtra("otp",binding.otpEt.text.toString())
                        startActivity(intent)

                    } else {
                        Comman.showLongToast(this, mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    Comman.showLongToast(this, response.error.toString())
                }
                is ApiCallback.Loading -> {
                    if (!response.isLoading) {
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }


}