package com.applligent.admitly.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.Repository
import com.applligent.admitly.databinding.ActivityStudentInfoBinding
import com.applligent.admitly.databinding.TestLayoutItemsBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.studentScreens.studentActivities.PostProjectActivity
import com.applligent.admitly.ui.viewmodel.SignupViewModel
import com.applligent.admitly.ui.viewmodel.SignupViewModelFactory
import com.applligent.admitly.utils.Comman
import com.google.gson.Gson
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap


class StudentInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudentInfoBinding
    lateinit var bindingLayout: TestLayoutItemsBinding
    private lateinit var userName:String
    private lateinit var userEmail:String
    private lateinit var password:String
    lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("user_name").toString()
        userEmail = intent.getStringExtra("user_email").toString()
        password = intent.getStringExtra("password").toString()

        signupViewModel = ViewModelProvider(
            this,
            SignupViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(SignupViewModel::class.java)


        setListener()
        setObserVer()

    }

    private fun setListener(){

        binding.continueBtn.setOnClickListener {

            if(binding.schoolEt.text.toString().isEmpty()){
                binding.schoolEt.requestFocus()
                binding.schoolEt.setError("Enter school name!")
                return@setOnClickListener
            }else if(binding.yearEt.text.toString().isEmpty()){
                binding.yearEt.requestFocus()
                binding.yearEt.setError("Enter year!")
                return@setOnClickListener
            }else if(binding.majorEt.text.toString().isEmpty()){
                binding.majorEt.requestFocus()
                binding.majorEt.setError("Enter major!")
                return@setOnClickListener
            }else if(binding.gpaEt.text.toString().isEmpty()){
                binding.gpaEt.requestFocus()
                binding.gpaEt.setError("Enter GPA!")
                return@setOnClickListener
            }else{
                val signupMap = HashMap<String, Any>()
                signupMap.put("name",userName)
                signupMap.put("email",userEmail)
                signupMap.put("userType",1)
                signupMap.put("password",password)
                signupMap.put("deviceType","A")
                signupMap.put("deviceToken","...")
                signupMap.put("countryId",156)
                signupMap.put("school",binding.schoolEt.text.toString())
                signupMap.put("year",binding.yearEt.text.toString())
                signupMap.put("major",binding.majorEt.text.toString())
                signupMap.put("gpa",binding.gpaEt.text.toString())

                val testArray = ArrayList<HashMap<String, Any>>()
                val tests1 = HashMap<String, Any>()
                tests1.put("title","Maters in Arts")
                tests1.put("score",95)
                testArray.add(tests1)

                val tests2 = HashMap<String, Any>()
                tests2.put("title","Maters in Sociology")
                tests2.put("score",89)
                testArray.add(tests2)

                signupMap.put("tests",testArray)

                System.out.println("MY_DATA_IS "+signupMap.toString())
                signupViewModel.userSignup(signupMap)
            }


//            startActivity(Intent(this, PostProjectActivity::class.java))


        }



    }


    fun setObserVer() {
        signupViewModel.signupCallback.observe(this,
            { response ->
                when (response) {
                    is ApiCallback.Success -> {
                        val res = Gson().toJson(response.data)
                        val mainObject = JSONObject(res)
                        System.out.println("SIGNUP_RESPONSE_IS success "+mainObject.toString())
                        if(mainObject.getBoolean("success")){
                            Comman.showLongToast(this,"Registration Success")
                            // TODO need to save registration data
                            startActivity(Intent(this, PostProjectActivity::class.java))
                        }else{
                            Comman.showLongToast(this,mainObject.getString("message"))
                        }
                    }
                    is ApiCallback.Error -> {
                        System.out.println("SIGNUP_RESPONSE_IS error "+response.error)
                    }
                    is ApiCallback.Loading -> {
                        if(!response.isLoading){
                            System.out.println("SIGNUP_RESPONSE_IS Loading false ")
                        }
                    }
                }
            })
    }
}