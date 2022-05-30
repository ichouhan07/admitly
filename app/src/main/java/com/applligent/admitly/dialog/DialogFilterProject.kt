package com.applligent.admitly.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatCheckBox
import com.applligent.admitly.R

class DialogFilterProject(private val activity: Activity) : Dialog(activity) {
    var applyFilterOnCurrentProject: () -> Unit = {}
    var applyFilterOnPastProject: () -> Unit = {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        setUpObservers()
    }

    private fun initViews() {
        setUpDialogView()
    }

    private fun setUpObservers() {
        val cbCheck1 = findViewById<AppCompatCheckBox>(R.id.cb_check1)
        val cbCheck2 = findViewById<AppCompatCheckBox>(R.id.cb_check2)
        val dismissBtn = findViewById<ImageView>(R.id.dismiss_btn)

        cbCheck1.setOnClickListener {
            if (cbCheck1.isChecked){
                cbCheck2.isChecked = false
                applyFilterOnCurrentProject()
                dismiss()
            }
        }
        cbCheck2.setOnClickListener {
            if (cbCheck2.isChecked){
                cbCheck1.isChecked = false
                applyFilterOnPastProject()
                dismiss()
            }
        }
        dismissBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpDialogView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_filter_project)
        window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}