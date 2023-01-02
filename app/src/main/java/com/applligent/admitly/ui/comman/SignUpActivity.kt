package com.applligent.admitly.ui.comman

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applligent.admitly.databinding.ActivitySignUpBinding
import com.applligent.admitly.ui.counselor.CounselorSignupActivity
import com.applligent.admitly.ui.student.StudentInfoActivity

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private var userType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userType = intent.getIntExtra("user_type",1)
        setListeners()

    }
    private fun setListeners(){
        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.alreadyHaveAccount.setOnClickListener {
            val i = Intent(this, SignInActivity::class.java)
            startActivity(i)
        }
        binding.signUpBtn.setOnClickListener {
            if (isValidSignUpDetails() == true){
                if(userType == 1){
                    val intent = Intent(this, StudentInfoActivity::class.java)
                    intent.putExtra("user_name",binding.userNameSignUp.text.toString())
                    intent.putExtra("user_email",binding.emailSignUp.text.toString())
                    intent.putExtra("password",binding.passwordSignUp.text.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }else{
                    val intent = Intent(this, CounselorSignupActivity::class.java)
                    intent.putExtra("user_name",binding.userNameSignUp.text.toString())
                    intent.putExtra("user_email",binding.emailSignUp.text.toString())
                    intent.putExtra("password",binding.passwordSignUp.text.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

            }

        }
    }

    private fun isValidSignUpDetails(): Boolean {
        return if (binding.userNameSignUp.text.toString().isEmpty()) {
            binding.userNameSignUp.requestFocus()
            binding.userNameSignUp.error = "Please enter name!"
            false
        } else if (binding.emailSignUp.text.toString().isEmpty()) {
            binding.emailSignUp.requestFocus()
            binding.emailSignUp.error = "Please enter email!"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailSignUp.text.toString()).matches()) {
            binding.emailSignUp.requestFocus()
            binding.emailSignUp.error = "Please enter valid email!"
            false
        } else if (binding.passwordSignUp.text.toString().isEmpty()) {
            binding.passwordSignUp.requestFocus()
            binding.passwordSignUp.error = "Please enter password!"
            false
        } else if(binding.passwordSignUp.text.toString().length < 6){
            binding.passwordSignUp.requestFocus()
            binding.passwordSignUp.error = "Password must be 6 digits!"
            false
        }else{
            true
        }
    }
}