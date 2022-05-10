package com.applligent.admitly

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applligent.admitly.databinding.ActivitySignUpBinding
import com.applligent.admitly.studentScreens.studentActivities.StudentInfoActivity

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()

    }
    private fun setListeners(){
        binding.alreadyHaveAccount.setOnClickListener {
            val i = Intent(this,SignInActivity::class.java)
            startActivity(i)
        }
        binding.signUpBtn.setOnClickListener {
            if (isValidSignUpDetails() == true){
                startActivity(Intent(this,StudentInfoActivity::class.java))
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun isValidSignUpDetails(): Boolean? {
        return if (binding.userNameSignUp.text.toString().trim().isEmpty()) {
            showToast("Please enter name")
            false
        } else if (binding.emailSignUp.text.toString().trim().isEmpty()) {
            showToast("Please enter email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailSignUp.text.toString())
                .matches()
        ) {
            showToast("Please enter valid email")
            false
        } else if (binding.passwordSignUp.text.toString().trim().isEmpty()) {
            showToast("Please enter password")
            false
        } /*else if (binding.passwordSignUp.text.toString() > 7.toString()) {
            showToast("Please enter 7 digit password")
            false
        }*/ else {
            true
        }
    }
}