package com.applligent.admitly.ui.counselor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applligent.admitly.databinding.ActivityConPaymentMethodBinding
import com.applligent.admitly.dialog.DialogSuccessfullyWithdraw
import com.applligent.admitly.dialog.DialogWithdrawAmount

class ConPaymentMethodActivity : AppCompatActivity() {
    lateinit var binding: ActivityConPaymentMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
    }

    private fun setListener() {
        binding.backBtn2.setOnClickListener { onBackPressed() }
        binding.addBankAccount.setOnClickListener {
            val intent = Intent(this,AddBankAccountActivity::class.java)
            startActivity(intent)
        }
        binding.withdrawBtn.setOnClickListener {
            val dialogWithdrawAmount = DialogWithdrawAmount(this)
            dialogWithdrawAmount.show()
            dialogWithdrawAmount.confirmWithdrawButton = {
                val dialogSuccessfullyWithdraw = DialogSuccessfullyWithdraw(this)
                dialogSuccessfullyWithdraw.show()
                dialogWithdrawAmount.dismiss()
            }
        }
    }
}