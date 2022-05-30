package com.applligent.admitly.ui.comman

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.databinding.ActivityUpdatePasswordBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.viewmodel.UpdatePasswordViewModel
import com.applligent.admitly.viewmodel.UpdatePasswordViewModelFactory
import com.google.gson.Gson
import org.json.JSONObject

class UpdatePasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdatePasswordBinding
    lateinit var updatePasswordViewModel: UpdatePasswordViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var userOtp:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userOtp = intent.getStringExtra("otp").toString()

        updatePasswordViewModel =  ViewModelProvider(
            this,
            UpdatePasswordViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(UpdatePasswordViewModel::class.java)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")

        setListeners()
        setObserVer()
    }

    private fun checkPassword(): Boolean? {
         if (binding.etNewPassword.text.toString().trim().isEmpty()){
            binding.etNewPassword.requestFocus()
            binding.etNewPassword.setError("Please enter password")

        } else if(binding.etNewPassword.text.length < 6){
            binding.etNewPassword.requestFocus()
            binding.etNewPassword.setError("Password is short")
        } else if (binding.etNewPassword.text.toString() != binding.etCNewPassword.text.toString()){
            binding.etCNewPassword.requestFocus()
            binding.etCNewPassword.setError("Password not matches")
        }else{
           return true
        }
        return false
    }

    fun setListeners(){
        binding.updatePassSubmitBtn.setOnClickListener {
            if (checkPassword() == true)
            {
                progressDialog.show()
                val passMap = HashMap<String, Any>()
                passMap.put("otp",userOtp)
                passMap.put("newpassword",binding.etCNewPassword.text.toString().trim())
                System.out.println("MY_RESET_REQ "+passMap.toString())
                updatePasswordViewModel.resetPassword(passMap)
            }
        }
    }

    fun setObserVer(){
        updatePasswordViewModel.updatePasswordCallback.observe(this
        ) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    // System.out.println("LOGIN_RESPONSE_IS "+mainObject.toString())
                    if (mainObject.getBoolean("success")) {
                        Comman.showLongToast(this, "Password changed success")
                        val intent = Intent(this,SignInActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
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