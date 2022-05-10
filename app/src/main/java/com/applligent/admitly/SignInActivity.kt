package com.applligent.admitly

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.databinding.ActivitySignInBinding
import com.applligent.admitly.studentScreens.studentActivities.StudentInfoActivity
import retrofit2.create

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    //var signInViewModel: SignInViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        /*signInViewModel = ViewModelProvider(this, SignInViewModelFactory(Repository(ApiInterface.getInstance(this).create()))).get(SignInViewModel::class.java)
        signInViewModel!!.getAllCountries()*/
    }
    private fun setListeners(){
        binding.dontHaveAccount.setOnClickListener {
            val i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
        }
        binding.signInBtn.setOnClickListener {
            if (isValidSignUpDetails() == true){
                startActivity(Intent(this, StudentInfoActivity::class.java))
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun isValidSignUpDetails(): Boolean? {
        return if (binding.emailSignIn.text.toString().trim().isEmpty()) {
            showToast("Please enter email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailSignIn.text.toString())
                .matches()
        ) {
            showToast("Please enter valid email")
            false
        } else if (binding.passwordSignIn.text.toString().trim().isEmpty()) {
            showToast("Please enter password")
            false
        } /*else if (binding.passwordSignIn.text.toString() > 7.toString()) {
            showToast("Please enter 7 digit password")
            false
        }*/ else {
            true
        }
    }
}