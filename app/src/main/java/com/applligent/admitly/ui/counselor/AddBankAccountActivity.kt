package com.applligent.admitly.ui.counselor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applligent.admitly.R
import com.applligent.admitly.databinding.ActivityAddbankAccountBinding
import com.applligent.admitly.utils.toast

class AddBankAccountActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddbankAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddbankAccountBinding.inflate(layoutInflater)
        setListener()

        setContentView(binding.root)
    }

    private fun setListener() {
        binding.backBtn3.setOnClickListener { onBackPressed() }
        binding.addBankBtn.setOnClickListener {
            if (isValidServiceDetails()){
                toast("Its Done")
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
}