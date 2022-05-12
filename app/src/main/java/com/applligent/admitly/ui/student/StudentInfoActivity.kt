package com.applligent.admitly.ui.student

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.adapter.CountrySpinnerAdapter
import com.applligent.admitly.databinding.ActivityStudentInfoBinding
import com.applligent.admitly.databinding.TestLayoutItemsBinding
import com.applligent.admitly.model.CountryModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.viewmodel.SignupViewModel
import com.applligent.admitly.viewmodel.SignupViewModelFactory
import com.applligent.admitly.utils.Comman
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap


class StudentInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudentInfoBinding
    lateinit var bindingLayout: TestLayoutItemsBinding
    private lateinit var userName:String
    private lateinit var userEmail:String
    private lateinit var password:String
    private var countryId: Int = 0
    lateinit var signupViewModel: SignupViewModel
    var countrySpinnerAdapter: CountrySpinnerAdapter? = null
    private lateinit var countryList: ArrayList<CountryModel?>

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

        countryList = ArrayList<CountryModel?>()

        setListener()
        setObserVer()
        signupViewModel.getAllCountry()

    }

    private fun setListener(){

        binding.countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    countryId = countryList[position]?.id!!
                    System.out.println("MY_COUNTRY "+countryId)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }


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
                signupMap.put("deviceToken","")
                signupMap.put("countryId",countryId)
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
                                countrySpinnerAdapter = CountrySpinnerAdapter(this@StudentInfoActivity, countryList)
                                binding.countrySpinner.adapter = countrySpinnerAdapter
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