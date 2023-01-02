package com.applligent.admitly.ui.student

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.databinding.ActivityAddPaymentMethodBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.utils.preferences.getToken
import com.applligent.admitly.utils.toast
import com.applligent.admitly.viewmodel.StudentPaymentViewModel
import com.applligent.admitly.viewmodel.StudentPaymentViewModelFactory
import com.google.gson.Gson
import org.json.JSONObject

class AddPaymentMethodActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPaymentMethodBinding
    private lateinit var cardViewModel: StudentPaymentViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cardViewModel = ViewModelProvider(
            this,
            StudentPaymentViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(StudentPaymentViewModel::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")
        setListener()
        setObserver()
    }
    private fun setListener() {
        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.saveCardBtn.setOnClickListener {
            if (isValidCardDetails()){
                val cardMap = HashMap<String, Any>()
                cardMap["cardNumber"] = binding.cardNumberEt.text.toString().trim()
                cardMap["cardHolderName"] = binding.cardHolderNameEt.text.toString().trim()
                cardMap["cardExpiryDate"] = binding.expiredEt.text.toString().trim()
                cardViewModel.getAddCard(cardMap,getToken())
            }
        }
    }
    private fun isValidCardDetails(): Boolean {
        return if (binding.cardHolderNameEt.text.toString().trim().isEmpty()){
            binding.cardHolderNameEt.requestFocus()
            binding.cardHolderNameEt.error = "Enter card holder name!"
            false
        }else if (binding.cardNumberEt.text.toString().trim().isEmpty()){
            binding.cardNumberEt.requestFocus()
            binding.cardNumberEt.error = "Enter card number!"
            false
        }else if (binding.expiredEt.text.toString().trim().isEmpty()){
            binding.expiredEt.requestFocus()
            binding.expiredEt.error = "Enter expired date!"
            false
        }else if (binding.cvvEt.text.toString().trim().isEmpty()){
            binding.cvvEt.requestFocus()
            binding.cvvEt.error = "Enter CVV!"
            false
        }else{
            true
        }
    }
    private fun setObserver() {
        cardViewModel.addCardCallback.observe(this) { response ->
            when(response){
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")){
                        toast(mainObject.getString("message"))
                        binding.cardNumberEt.text.clear()
                        binding.cardHolderNameEt.text.clear()
                        binding.expiredEt.text.clear()
                        binding.cvvEt.text.clear()
                    }else{
                        toast(mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    toast(response.error)
                }
                is ApiCallback.Loading -> {
                    progressDialog.dismiss()
                }
            }
        }
    }
}