package com.applligent.admitly.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import com.applligent.admitly.R

class DialogWithdrawAmount(private val activity: Activity) : Dialog(activity) {
    var confirmWithdrawButton: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        setUpObservers()
    }
    private fun initViews() {
        setUpDialogView()
    }
    private fun setUpObservers() {
        val enterAmount = findViewById<EditText>(R.id.et_enter_amount)
        val confirmWithdrawBtn = findViewById<AppCompatButton>(R.id.confirm_withdraw_btn)
        val dismissBtn = findViewById<ImageView>(R.id.dismiss_btn)
        confirmWithdrawBtn.setOnClickListener {
            confirmWithdrawButton()
        }
        dismissBtn.setOnClickListener {
            dismiss()
        }
    }
    private fun setUpDialogView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_withdraw_amount)
        window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}