package com.applligent.admitly.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import com.applligent.admitly.R

class DialogSureReleasePayment(private val activity: Activity) : Dialog(activity) {
    var confirmReleaseButton: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        setUpObservers()
    }
    private fun initViews() {
        setUpDialogView()
    }
    private fun setUpObservers() {
        val confirmReleaseBtn = findViewById<AppCompatButton>(R.id.confirm_release_btn)
        val dismissBtn = findViewById<ImageView>(R.id.dismiss_btn)
        confirmReleaseBtn.setOnClickListener {
            confirmReleaseButton()
        }
        dismissBtn.setOnClickListener {
            dismiss()
        }

    }
    private fun setUpDialogView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_sure_release_payment)
        window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}