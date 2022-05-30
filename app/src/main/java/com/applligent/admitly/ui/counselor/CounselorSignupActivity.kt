package com.applligent.admitly.ui.counselor

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.R
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.adapter.CountrySpinnerAdapter
import com.applligent.admitly.chatSystem.model.Users
import com.applligent.admitly.databinding.ActivityCounselorSignupBinding
import com.applligent.admitly.model.CountryModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.student.PostProjectActivity
import com.applligent.admitly.viewmodel.SignupViewModel
import com.applligent.admitly.viewmodel.SignupViewModelFactory
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.utils.preferences.*
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
import java.util.ArrayList
import java.util.HashMap

class CounselorSignupActivity : AppCompatActivity() {
    lateinit var binding: ActivityCounselorSignupBinding


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
        binding = ActivityCounselorSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  setContentView(R.layout.activity_counselor_signup)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()


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

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")

        setObserver()
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


        progressDialog.show()
        signupViewModel.getAllCountry()


        binding.continueCounselorBtn .setOnClickListener {

            if (binding.occupationEt.text.toString().isEmpty()) {
                binding.occupationEt.requestFocus()
                binding.occupationEt.error = "Enter occupation!"
                return@setOnClickListener
            } else if (binding.linkedinUrlEt.text.toString().isEmpty()) {
                binding.linkedinUrlEt.requestFocus()
                binding.linkedinUrlEt.error = "Enter linkedinUrl!"
                return@setOnClickListener
            } else {
                progressDialog.show()
                val signupMap = HashMap<String, Any>()
                signupMap.put("name",userName)
                signupMap.put("email",userEmail)
                signupMap.put("userType",2)
                signupMap.put("password",password)
                signupMap.put("deviceType","A")
                signupMap.put("deviceToken","...")
                signupMap.put("countryId",countryId)
                signupMap.put("occupation",binding.occupationEt.text.toString().trim())
                signupMap.put("linkedInUrl",binding.linkedinUrlEt.text.toString().trim())

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

    fun setObserver() {
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
                        val intent = Intent(this, MyServiceActivity::class.java)
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
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                System.out.println("MY_COUNTRY success " + jsonObject.getString("name"))
                                val countryModel = CountryModel(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("iso3")
                                )
                                countryList.add(countryModel)
                            }
                            countrySpinnerAdapter =
                                CountrySpinnerAdapter(this@CounselorSignupActivity, countryList)
                            binding.countrySpinner.adapter = countrySpinnerAdapter
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        Comman.showLongToast(this, mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    println("COUNTRY error " + response.error)
                    progressDialog.dismiss()
                    Comman.showLongToast(this, response.error)
                }
                is ApiCallback.Loading -> {
                    if (!response.isLoading) {
                        progressDialog.dismiss()
                    }
                }
            }
        }

    }
}