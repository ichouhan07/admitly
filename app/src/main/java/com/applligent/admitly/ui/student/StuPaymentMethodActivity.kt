package com.applligent.admitly.ui.student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applligent.admitly.R
import com.applligent.admitly.databinding.ActivityStuPaymentMethodBinding

class StuPaymentMethodActivity : AppCompatActivity() {
    lateinit var binding: ActivityStuPaymentMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStuPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
    }

    private fun setListener() {
        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.addPaymentMethod.setOnClickListener {
            val intent = Intent(this,AddPaymentMethodActivity::class.java)
            startActivity(intent)
        }
    }
}