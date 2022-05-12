package com.applligent.admitly.ui.counselor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.R
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.adapter.CountrySpinnerAdapter
import com.applligent.admitly.model.CountryModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.student.PostProjectActivity
import com.applligent.admitly.viewmodel.SignupViewModel
import com.applligent.admitly.viewmodel.SignupViewModelFactory
import com.applligent.admitly.utils.Comman
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class CounselorSignupActivity : AppCompatActivity() {


    private lateinit var userName:String
    private lateinit var userEmail:String
    private lateinit var password:String
    lateinit var signupViewModel: SignupViewModel
    var countrySpinnerAdapter: CountrySpinnerAdapter? = null
    private lateinit var countryList: ArrayList<CountryModel?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counselor_signup)

        initialize()
    }

    fun initialize(){

        userName = intent.getStringExtra("user_name").toString()
        userEmail = intent.getStringExtra("user_email").toString()
        password = intent.getStringExtra("password").toString()
        countryList = ArrayList<CountryModel?>()

        signupViewModel = ViewModelProvider(
            this,
            SignupViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(SignupViewModel::class.java)


        var signup_btn:Button = findViewById(R.id.signup_btn)

        setObserVer()

        signup_btn.setOnClickListener {
            val signupMap = HashMap<String, Any>()
            signupMap.put("name",userName)
            signupMap.put("email",userEmail)
            signupMap.put("userType",2)
            signupMap.put("password",password)
            signupMap.put("deviceType","A")
            signupMap.put("deviceToken","...")
            signupMap.put("countryId",101)
            signupMap.put("occupation","occupation")
            signupMap.put("linkedInUrl","linkedInUrl")

            signupViewModel.userSignup(signupMap)

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

        signupViewModel.countryCallback.observe(this,
            { response ->
                when (response) {
                    is ApiCallback.Success -> {
                        val res = Gson().toJson(response.data)
                        val mainObject = JSONObject(res)
                        System.out.println("COUNTRY success "+mainObject.toString())
                        if(mainObject.getBoolean("success")){
                            try {
                                val jsonArray = mainObject.getJSONArray("data");
                                for (i in 0..jsonArray.length()-1) {
                                    val jsonObject = jsonArray.getJSONObject(i)
                                    val countryModel = CountryModel(jsonObject.getInt("id"),jsonObject.getString("name"),jsonObject.getString("iso3"))
                                    countryList.add(countryModel)
                                }
                                countrySpinnerAdapter = CountrySpinnerAdapter(this@CounselorSignupActivity, countryList)
                               // binding.countrySpinner.adapter = countrySpinnerAdapter
                            }catch (e:JSONException){
                                e.printStackTrace()
                            }
                        }else{
                            Comman.showLongToast(this,mainObject.getString("message"))
                        }
                    }
                    is ApiCallback.Error -> {
                        System.out.println("COUNTRY error "+response.error)
                    }
                    is ApiCallback.Loading -> {
                        if(!response.isLoading){
                            System.out.println("COUNTRY Loading false ")
                        }
                    }
                }
            })

    }
}