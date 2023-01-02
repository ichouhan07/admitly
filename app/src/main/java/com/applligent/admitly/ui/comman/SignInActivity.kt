package com.applligent.admitly.ui.comman

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.databinding.ActivitySignInBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.counselor.CounselorDashboardActivity
import com.applligent.admitly.ui.student.StudentDashboardActivity
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.utils.log
import com.applligent.admitly.utils.preferences.*
import com.applligent.admitly.viewmodel.LoginViewModel
import com.applligent.admitly.viewmodel.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.json.JSONObject


class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var progressDialog: ProgressDialog
    private var userType: Int = 2

    //Login with google
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100

    //for msg
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(LoginViewModel::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")

        setListeners()
        setObserver()
        //Login with google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

       // Check for existing Google Sign In account, if the user is already signed in
       // the GoogleSignInAccount will be non-null.
       val account = GoogleSignIn.getLastSignedInAccount(this)
        /*signInViewModel = ViewModelProvider(this, SignInViewModelFactory(Repository(ApiInterface.getInstance(this).create()))).get(SignInViewModel::class.java)
        signInViewModel!!.getAllCountries()*/
    }

    private fun setListeners() {
        binding.dontHaveAccount.setOnClickListener {
            val i = Intent(this, ChooseActivity::class.java)
            startActivity(i)
        }
        binding.signInBtn.setOnClickListener {
            if (isValidSignUpDetails()) {
                progressDialog.show()
                val loginMap = HashMap<String, Any>()
                loginMap.put("email",binding.emailSignIn.text.toString().trim())
                loginMap.put("password",binding.passwordSignIn.text.toString().trim())
                loginMap.put("loginType",1)
                loginViewModel.userLogin(loginMap)
                //for msg
                val signInEmail = binding.emailSignIn.toString().trim()
                val signInPass = binding.passwordSignIn.text.toString().trim()
                auth.signInWithEmailAndPassword(signInEmail,signInPass).addOnCompleteListener {task->
                    if (task.isSuccessful)
                    {
                        //toast("User Found")
                    }
                    else
                    {
                        //toast("User Not Found")
                    }
                }
            }
        }
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
        binding.googleSignInBtn.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent,RC_SIGN_IN)
        }
    }

    fun setObserver() {
        loginViewModel.loginCallback.observe(this) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        val data = mainObject.getJSONObject("data")
                        setUserId(data.getInt("userId"))
                        setToken(data.getString("token"))
                        setLoginStatus(true)
                        setUserType(data.getInt("userType"))
                        setAccountType(data.getInt("accountType"))
                        Comman.showLongToast(this, "Login Success")
                        // TODO need to save login data
                        if(mainObject.getJSONObject("data").getInt("userType") == 1){
                            val intent = Intent(this, StudentDashboardActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }else{
                            val intent = Intent(this, CounselorDashboardActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Comman.showLongToast(this, mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    Comman.showLongToast(this, response.error.toString())
                }
                is ApiCallback.Loading -> {
                    if (!response.isLoading) {
                      0
                    }
                }
            }
        }
    }

    private fun isValidSignUpDetails(): Boolean {
        return if (binding.emailSignIn.text.toString().trim().isEmpty()) {
            binding.emailSignIn.requestFocus()
            binding.emailSignIn.error = "Enter email address!"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailSignIn.text.toString().trim())
                .matches()
        ) {
            binding.emailSignIn.requestFocus()
            binding.emailSignIn.error = "Enter valid email!"
            false
        } else if (binding.passwordSignIn.text.toString().trim().isEmpty()) {
            binding.passwordSignIn.requestFocus()
            binding.passwordSignIn.error = "Enter password!"
            false
        }  else if(binding.passwordSignIn.text.toString().length < 6){
            binding.passwordSignIn.requestFocus()
            binding.passwordSignIn.error = "Password must be 6 digits!"
            false
        } else{
            true
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                var personName = acct.displayName
                var personGivenName = acct.givenName
                var personFamilyName = acct.familyName
                var personEmail = acct.email
                var personId = acct.id
                var personPhoto = acct.photoUrl

                progressDialog.show()
                val loginMap = HashMap<String, Any>()
                loginMap["socialId"] = personId.toString().trim()
                loginMap["name"] = personName.toString()
                loginMap["email"] = personEmail.toString().trim()
                loginMap["loginType"] = 2
                loginMap["profileUrl"] = "Url"
                loginMap["socialType"] = "Google"
                loginMap["deviceType"] = "A"
                loginMap["deviceToken"] = "----"
                loginMap["userType"] = userType

                loginViewModel.userLogin(loginMap)
            }

            // Signed in successfully, show authenticated UI.
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            log("fuguf"+ e.statusCode)
        }
    }
}