package com.applligent.admitly.ui.comman

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applligent.admitly.R
import com.applligent.admitly.databinding.ActivityChooseBinding

class ChooseActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseBinding
    var userType:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }
    private fun setListeners(){
        binding.chooseStudent.setOnClickListener {
            binding.chooseStudent.setImageResource(R.drawable.choose_stu)
            binding.chooseCounselor.setImageResource(R.drawable.not_choose_coun)
            userType = 1
        }
        binding.chooseCounselor.setOnClickListener {
            binding.chooseCounselor.setImageResource(R.drawable.choose_coun)
            binding.chooseStudent.setImageResource(R.drawable.not_choose_stu)
            userType = 2

        }
        binding.continueBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("user_type",userType)
            startActivity(intent)
            /*if (isValidSignUpDetails() == true){
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }*/
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    /*private fun isValidSignUpDetails(): Boolean? {
        return if (binding.chooseStudent.isSelected() || binding.chooseCounselor.isSelected() ) {
            return true
        } else {
            showToast("Select at least one")
            return false
        }
    }*/
}