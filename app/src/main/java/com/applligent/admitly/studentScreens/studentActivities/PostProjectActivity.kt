package com.applligent.admitly.studentScreens.studentActivities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applligent.admitly.R
import com.applligent.admitly.databinding.ActivityPostProjectBinding

class PostProjectActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostProjectBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectButton()

    }
    private fun selectButton(){
        binding.smMatchingBox.setOnClickListener {
            binding.smMatchingBox.setBackgroundResource(R.color.green)
            binding.smMatching.setImageResource(R.drawable.s_m_matching_select)
            binding.easyEditingBox.setBackgroundResource(R.color.white)
            binding.easyEditing.setImageResource(R.drawable.easy_editing)
        }
        binding.easyEditingBox.setOnClickListener {
            binding.easyEditingBox.setBackgroundResource(R.color.green)
            binding.easyEditing.setImageResource(R.drawable.easy_editing_select)
            binding.smMatchingBox.setBackgroundResource(R.color.white)
            binding.smMatching.setImageResource(R.drawable.s_m_matching)
        }
    }
}