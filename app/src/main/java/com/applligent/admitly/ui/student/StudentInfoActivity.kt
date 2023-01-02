package com.applligent.admitly.ui.student

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
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
import com.applligent.admitly.utils.preferences.*
import com.applligent.admitly.viewmodel.SignupViewModel
import com.applligent.admitly.viewmodel.SignupViewModelFactory
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class StudentInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudentInfoBinding
    private lateinit var userName:String
    private lateinit var userEmail:String
    private lateinit var password:String
    private var countryId: Int = 0
    private lateinit var signupViewModel: SignupViewModel
    var countrySpinnerAdapter: CountrySpinnerAdapter? = null
    private lateinit var countryList: ArrayList<CountryModel?>
    private lateinit var progressDialog: ProgressDialog

    //for msg
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var storage: FirebaseStorage
    lateinit var userId: String
    lateinit var imageUri: String
    //bitmap to string
    private var imageString = ""
    //upload image
    val REQUEST_IMAGE_CAPTURE = 1
    var SELECT_PICTURE = 200

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
        setObserver()

        progressDialog.show()
        signupViewModel.getAllCountry()

    }

    private fun setListener(){

        binding.ivUploadImg.setOnClickListener {
            selectImage()
        }
        binding.countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    //selectCountry if (position != 0)
                    if (position != 0)
                    countryId = countryList[position]?.id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        binding.addMoreTest.setOnClickListener {
            addView()
        }
        binding.continueBtn.setOnClickListener {
            if(binding.schoolEt.text.toString().isEmpty()){
                binding.schoolEt.requestFocus()
                binding.schoolEt.error = "Enter school name!"
                return@setOnClickListener
            }else if(binding.yearEt.text.toString().isEmpty()){
                binding.yearEt.requestFocus()
                binding.yearEt.error = "Enter year!"
                return@setOnClickListener
            }else if(binding.majorEt.text.toString().isEmpty()){
                binding.majorEt.requestFocus()
                binding.majorEt.error = "Enter major!"
                return@setOnClickListener
            }else if(binding.gpaEt.text.toString().isEmpty()){
                binding.gpaEt.requestFocus()
                binding.gpaEt.error = "Enter GPA!"
                return@setOnClickListener
            } else if(binding.testMajorEt.text.toString().isEmpty()){
                binding.testMajorEt.requestFocus()
                binding.testMajorEt.error = "Enter test major!"
                return@setOnClickListener
            }else if(binding.testScoreEt.text.toString().isEmpty()){
                binding.testScoreEt.requestFocus()
                binding.testScoreEt.error = "Enter test score!"
                return@setOnClickListener
            }else{
                progressDialog.show()
                val testArray = ArrayList<HashMap<String, Any>>()
                val mainMap = HashMap<String, Any>()
                mainMap["title"] = binding.testMajorEt.text.toString()
                mainMap["score"] = binding.testScoreEt.text.toString()
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
                signupMap["name"] = userName
                signupMap["email"] = userEmail
                signupMap["userType"] = 1
                signupMap["password"] = password
                signupMap["deviceType"] = "A"
                signupMap["deviceToken"] = ""
                signupMap["countryId"] = countryId
                signupMap["school"] = binding.schoolEt.text.toString()
                signupMap["year"] = binding.yearEt.text.toString()
                signupMap["major"] = binding.majorEt.text.toString()
                signupMap["gpa"] = binding.gpaEt.text.toString()
                signupMap["profileImage"] = imageString

                signupMap["tests"] = testArray
                //println("MY_DATA_IS $signupMap")
                signupViewModel.userSignup(signupMap)

            }
        }
    }

    //upload image
    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val f = File(Environment.getExternalStorageDirectory(), "temp.jpg")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
                dispatchTakePictureIntent()
            } else if (options[item] == "Choose from Gallery") {
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                imageChooser()
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }
    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data!!.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            imageString = bitMapToString(imageBitmap)
            binding.ivProfileImg.setImageBitmap(imageBitmap)

        } else if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data!!.data
                var imageBitmap: Bitmap? = null
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                imageString = bitMapToString(imageBitmap)
                if (null != selectedImageUri) {
                    binding.ivProfileImg.setImageURI(selectedImageUri)
                }
            }
        }
    }
    //upload image
    private fun bitMapToString(bitmap: Bitmap?): String {
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun addView() {
        val bindingLayout = TestLayoutItemsBinding.inflate(layoutInflater,null,false)
        bindingLayout.deleteTest.setOnClickListener {
            binding.testEmptyLayout.removeView(bindingLayout.root)
        }
        binding.testEmptyLayout.addView(bindingLayout.root)
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
                        //for msg
                        val pass =  password
                        auth.createUserWithEmailAndPassword(userEmail,pass).addOnCompleteListener { task: Task<AuthResult> ->
                            if (task.isSuccessful){
                                val message = "Hiii"
                                imageUri = "https://firebasestorage.googleapis.com/v0/b/admilty.appspot.com/o/Ellipse%209.png?alt=media&token=8f5c9209-a9e0-4f17-aa1c-b49b565a7cde"
                                val refrence: DatabaseReference = database.reference.child("user").child(userId)
                                storage.reference.child("upload").child(userId)

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
                        println("SIGNUP_RESPONSE_IS Loading false ")
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
                            val jsonArray = mainObject.getJSONArray("data")
                            for (i in 0 until jsonArray.length() + 1) {
                                //selectCountry (+ 1) if (i == 0 ) or else(- 1)!
                                if (i == 0 ){
                                    val countryModel = CountryModel(
                                        0, "Select country", ""
                                    )
                                    countryList.add(countryModel)
                                }else{
                                    val jsonObject = jsonArray.getJSONObject(i - 1)
                                    val countryModel = CountryModel(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("iso3")
                                    )
                                    countryList.add(countryModel)
                                }
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
                        println("COUNTRY Loading false ")
                    }
                }
            }
        }
    }
}