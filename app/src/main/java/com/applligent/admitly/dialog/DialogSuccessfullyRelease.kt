package com.applligent.admitly.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import com.applligent.admitly.R

class DialogSuccessfullyRelease(private val activity: Activity) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        setUpObservers()
    }
    private fun initViews() {
        setUpDialogView()
    }
    private fun setUpObservers() {
        val doneReleased = findViewById<AppCompatButton>(R.id.done_released_btn)
        doneReleased.setOnClickListener { dismiss() }
    }
    private fun setUpDialogView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_payment_successfully_released)
        window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}