package com.applligent.admitly.ui.student

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.R
import com.applligent.admitly.adapter.CountrySpinnerAdapter
import com.applligent.admitly.chatSystem.model.Users
import com.applligent.admitly.databinding.ActivityStudentInfoBinding
import com.applligent.admitly.databinding.TestLayoutItemsBinding
import com.applligent.admitly.model.CountryModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.utils.log
import com.applligent.admitly.utils.preferences.*
import com.applligent.admitly.viewmodel.SignupViewModel
import com.applligent.admitly.viewmodel.SignupViewModelFactory
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

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
    private lateinit var progressDialog: ProgressDialog

    //for msg
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var storage: FirebaseStorage
    lateinit var userId: String
    lateinit var imageUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

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
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")

        setListener()
        setObserVer()

        progressDialog.show()
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
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }


        binding.addMoreTest.setOnClickListener {
            addView();
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
            } else if(binding.testMajorEt.text.toString().isEmpty()){
                binding.testMajorEt.requestFocus()
                binding.testMajorEt.setError("Enter test major!")
                return@setOnClickListener
            }else if(binding.testScoreEt.text.toString().isEmpty()){
                binding.testScoreEt.requestFocus()
                binding.testScoreEt.setError("Enter test score!")
                return@setOnClickListener
            }else{

                progressDialog.show()
                val testArray = ArrayList<HashMap<String, Any>>()
                val mainMap = HashMap<String, Any>()
                mainMap.put("title",binding.testMajorEt.text.toString())
                mainMap.put("score",binding.testScoreEt.text.toString())
                testArray.add(mainMap)


                for (k in 0 until binding.testEmptyLayout.childCount) {
                    val selectLayout = binding.testEmptyLayout.getChildAt(k) as ConstraintLayout
                    val majorEt = selectLayout.findViewById<View>(R.id.row_major_et) as EditText
                    val scoreEt = selectLayout.findViewById<View>(R.id.row_score_et) as EditText

                    val hashMap = HashMap<String, Any>()
                    hashMap.put("title",majorEt.text.toString())
                    hashMap.put("score",scoreEt.text.toString())
                    testArray.add(hashMap)
                }

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

                signupMap.put("tests",testArray)

                System.out.println("MY_DATA_IS "+signupMap.toString())
                signupViewModel.userSignup(signupMap)

                //for msg
                val pass = "123456"
                auth.createUserWithEmailAndPassword(userEmail,pass).addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful){

                        var message = "Hiii"
                        imageUri = "https://firebasestorage.googleapis.com/v0/b/admilty.appspot.com/o/Ellipse%209.png?alt=media&token=8f5c9209-a9e0-4f17-aa1c-b49b565a7cde"
                        val refrence: DatabaseReference = database.getReference().child("user").child(userId)
                        val storageReference: StorageReference = storage.getReference().child("upload").child(userId)

                        val users = Users(userId,userName,userEmail,imageUri,message)
                        refrence.setValue(users).addOnCompleteListener {
                            if (it.isSuccessful){
                                //Toast.makeText(applicationContext, "firebase user created", Toast.LENGTH_SHORT).show()
                            }else{
                                //Toast.makeText(applicationContext, "firebase error", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else{

                    }
                }
            }
        }
    }
    private fun addView() {
        val bindingLayout = TestLayoutItemsBinding.inflate(layoutInflater,null,false)
        bindingLayout.deleteTest.setOnClickListener {

            binding.testEmptyLayout.removeView(bindingLayout.root)
        }
        binding.testEmptyLayout.addView(bindingLayout.root)
    }

    fun setObserVer() {
        signupViewModel.signupCallback.observe(this
        ) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        Comman.showLongToast(this, "Registration Success")
                        val data = mainObject.getJSONObject("data")
                        setUserId(data.getInt("userId"))
                        userId = getUserId().toString()
                        setToken(data.getString("token"))
                        setLoginStatus(true)
                        setUserType(data.getInt("userType"))
                        setAccountType(data.getInt("accountType"))
                        // TODO need to save registration data
                        val intent = Intent(this, PostProjectActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Comman.showLongToast(this, mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    Comman.showLongToast(this, response.error)
                }
                is ApiCallback.Loading -> {
                    if (!response.isLoading) {
                        progressDialog.dismiss()
                        System.out.println("SIGNUP_RESPONSE_IS Loading false ")
                    }
                }
            }
        }

        signupViewModel.countryCallback.observe(this
        ) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        try {
                            val jsonArray = mainObject.getJSONArray("data");
                            for (i in 0..jsonArray.length() - 1) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val countryModel = CountryModel(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("iso3")
                                )
                                countryList.add(countryModel)
                            }
                            countrySpinnerAdapter =
                                CountrySpinnerAdapter(this@StudentInfoActivity, countryList)
                            binding.countrySpinner.adapter = countrySpinnerAdapter
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        Comman.showLongToast(this, mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    Comman.showLongToast(this, response.error)
                }
                is ApiCallback.Loading -> {
                    if (!response.isLoading) {
                        progressDialog.dismiss()
                        System.out.println("COUNTRY Loading false ")
                    }
                }
            }
        }
    }
}