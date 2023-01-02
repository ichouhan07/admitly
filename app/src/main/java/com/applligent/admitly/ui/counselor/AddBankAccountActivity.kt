package com.applligent.admitly.ui.counselor

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.databinding.ActivityAddbankAccountBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.utils.preferences.getToken
import com.applligent.admitly.utils.toast
import com.applligent.admitly.viewmodel.CounselorPaymentViewModel
import com.applligent.admitly.viewmodel.CounselorPaymentViewModelFactory
import com.google.gson.Gson
import org.json.JSONObject

class AddBankAccountActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddbankAccountBinding
    lateinit var addAccountViewModel: CounselorPaymentViewModel
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddbankAccountBinding.inflate(layoutInflater)
        addAccountViewModel = ViewModelProvider(this,
            CounselorPaymentViewModelFactory(
                Repository(ApiClient().getClient()!!.create(ApiInterface::class.java))
            )
        ).get(CounselorPaymentViewModel::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")
        setListener()
        setObserver()

        setContentView(binding.root)
    }

    private fun setListener() {
        binding.backBtn3.setOnClickListener { onBackPressed() }
        binding.addBankBtn.setOnClickListener {
            if (isValidServiceDetails()){
                val addBankMap = HashMap<String,Any>()
                addBankMap["accountNumber"] = binding.accountNumberEt.text.toString().trim()
                addBankMap["accountHolderName"] = binding.accountHolderNameEt.text.toString().trim()
                addBankMap["bankName"] = binding.bankNameEt.text.toString().trim()
                addBankMap["accountSwiftKey"] = binding.swiftCodeEt.text.toString().trim()
                addAccountViewModel.getAddAccount(addBankMap,getToken())
            }
        }
    }
    private fun isValidServiceDetails(): Boolean {
        return if (binding.accountHolderNameEt.text.toString().trim().isEmpty()) {
            binding.accountHolderNameEt.requestFocus()
            binding.accountHolderNameEt.error = "Enter holder name!"
            false
        } else if (binding.bankNameEt.text.toString().trim().isEmpty()) {
            binding.bankNameEt.requestFocus()
            binding.bankNameEt.error = "Enter bank name!"
            false
        }else if (binding.accountNumberEt.text.toString().trim().isEmpty()) {
            binding.accountNumberEt.requestFocus()
            binding.accountNumberEt.error = "Enter account number!"
            false
        }else if (binding.swiftCodeEt.text.toString().trim().isEmpty()) {
            binding.swiftCodeEt.requestFocus()
            binding.swiftCodeEt.error = "Enter swift code!"
            false
        } else {
            true
        }
    }
    private fun setObserver() {
        addAccountViewModel.addBankCallBack.observe(this){response ->
            when(response){
                is ApiCallback.Success ->{
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")){
                        toast(mainObject.getString("message"))
                        binding.accountHolderNameEt.text.clear()
                        binding.accountNumberEt.text.clear()
                        binding.bankNameEt.text.clear()
                        binding.swiftCodeEt.text.clear()
                    }else{
                        toast(mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error ->{
                    progressDialog.dismiss()
                    toast(response.error)
                }
                is ApiCallback.Loading ->{
                    progressDialog.dismiss()
                }
            }
        }
    }
}