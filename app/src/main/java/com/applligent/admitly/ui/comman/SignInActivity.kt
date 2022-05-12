package com.applligent.admitly.ui.comman

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.databinding.ActivitySignInBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.student.StudentInfoActivity
import com.applligent.admitly.viewmodel.LoginViewModel
import com.applligent.admitly.viewmodel.LoginViewModelFactory
import com.applligent.admitly.utils.Comman
import com.google.gson.Gson
import org.json.JSONObject
import java.util.HashMap

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    //var signInViewModel: SignInViewModel? = null

    lateinit var loginViewModel: LoginViewModel
    private var userType: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userType = intent.getIntExtra("user_type",1)


        loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(LoginViewModel::class.java)


        setListeners()
        setObserVer()
        /*signInViewModel = ViewModelProvider(this, SignInViewModelFactory(Repository(ApiInterface.getInstance(this).create()))).get(SignInViewModel::class.java)
        signInViewModel!!.getAllCountries()*/
    }

    private fun setListeners() {
        binding.dontHaveAccount.setOnClickListener {
            val i = Intent(this, SignUpActivity::class.java)
            i.putExtra("user_type",userType)
            startActivity(i)
        }
        binding.signInBtn.setOnClickListener {
            if (isValidSignUpDetails() == true) {
                //startActivity(Intent(this, StudentInfoActivity::class.java))
                val loginMap = HashMap<String, Any>()
                loginMap.put("email",binding.emailSignIn.text.toString().trim())
                loginMap.put("password",binding.passwordSignIn.text.toString().trim())
                loginMap.put("loginType",1)
                loginViewModel.userLogin(loginMap)
            }
        }
    }

    fun setObserVer() {
        loginViewModel.loginCallback.observe(this,
            { response ->
                when (response) {
                    is ApiCallback.Success -> {
                        val res = Gson().toJson(response.data)
                        val mainObject = JSONObject(res)
                       // System.out.println("LOGIN_RESPONSE_IS "+mainObject.toString())
                        if(mainObject.getBoolean("success")){
                            Comman.showLongToast(this,"Login Success")
                            // TODO need to save login data
                            startActivity(Intent(this, StudentInfoActivity::class.java))
                        }else{
                            Comman.showLongToast(this,mainObject.getString("message"))
                        }
                    }
                    is ApiCallback.Error -> {
                        System.out.println("MY_DATA_IS Error "+  response.error)
                    }
                    is ApiCallback.Loading -> {
                        if(!response.isLoading){
                            System.out.println("MY_DATA_IS Loading false ")
                        }
                    }
                }
            })
    }


    private fun isValidSignUpDetails(): Boolean? {
        return if (binding.emailSignIn.text.toString().trim().isEmpty()) {
            binding.emailSignIn.requestFocus()
            binding.emailSignIn.setError("Please enter email address!")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailSignIn.text.toString())
                .matches()
        ) {
            binding.emailSignIn.requestFocus()
            binding.emailSignIn.setError("Please enter valid email!")
            false
        } else if (binding.passwordSignIn.text.toString().trim().isEmpty()) {
            binding.passwordSignIn.requestFocus()
            binding.passwordSignIn.setError("Please enter password!")
            false
        }  else if(binding.passwordSignIn.text.toString().length < 6){
            binding.passwordSignIn.requestFocus()
            binding.passwordSignIn.setError("Password must be 6 digits!")
            false
        } else{
            true
        }
    }
}