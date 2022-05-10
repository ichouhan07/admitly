package com.applligent.admitly.studentScreens.studentActivities

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applligent.admitly.databinding.ActivityStudentInfoBinding
import com.applligent.admitly.databinding.TestLayoutItemsBinding


class StudentInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudentInfoBinding
    lateinit var bindingLayout: TestLayoutItemsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        addLayout("Student name",10)
    }
    private fun addLayout(s: String, i: Int) {
        bindingLayout = TestLayoutItemsBinding.inflate(layoutInflater,binding.testEmptyLayout,false)
        bindingLayout.stuTestName
        bindingLayout.stuTestscore

        binding.testEmptyLayout.addView(bindingLayout.root)
    }

    private fun setListener(){
        binding.continueInfoBtn.setOnClickListener {
            startActivity(Intent(this,PostProjectActivity::class.java))
        }
    }
}