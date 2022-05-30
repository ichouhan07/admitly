package com.applligent.admitly.ui.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applligent.admitly.databinding.ActivityAddPaymentMethodBinding
import com.applligent.admitly.utils.toast

class AddPaymentMethodActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPaymentMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
    }

    private fun setListener() {
        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.saveCardBtn.setOnClickListener {
            if (isValidCardDetails() == true){
                toast("It's Done")
            }
        }

    }
    private fun isValidCardDetails(): Boolean? {
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
}