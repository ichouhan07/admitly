package com.applligent.admitly.ui.student

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.R
import com.applligent.admitly.databinding.ActivityHireCounselorProfileBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.utils.preferences.getToken
import com.applligent.admitly.utils.toast
import com.applligent.admitly.viewmodel.StudentDBViewModel
import com.applligent.admitly.viewmodel.StudentDBViewModelFactory
import com.google.gson.Gson
import org.json.JSONObject

class HireCounselorProfileActivity : AppCompatActivity() {
    lateinit var binding:ActivityHireCounselorProfileBinding
    private var counsellorId: Int = 0
    private var projectId: Int = 0
    private lateinit var sdbViewModel: StudentDBViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHireCounselorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        counsellorId = intent.getIntExtra("counsellorId",0)
        projectId = intent.getIntExtra("projectId",0)

        sdbViewModel = ViewModelProvider(this,
            StudentDBViewModelFactory(
                Repository(ApiClient().getClient()!!.create(ApiInterface::class.java))
            )
        ).get(StudentDBViewModel::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")
        setListener()
        setObserver()
    }

    private fun setListener() {
        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.hireCounselorBtn.setOnClickListener {
            progressDialog.show()
            //for testing token
            val token1: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIzIiwiZW1haWwiOiJ0ZXN0MTExQHRlc3QuY29tIiwidXNlclR5cGUiOiIxIn0.vDqOR7KLdO1bIcBKMVIPiUzttW0dejYsYzickWPd6do"
            val hireMap = HashMap<String, Any>()
            hireMap["counsellorId"] = counsellorId
            hireMap["projectId"] = projectId
            sdbViewModel.getHireCounselor(hireMap,getToken())
        }
    }
    private fun setObserver() {
        sdbViewModel.hireCounselorCallback.observe(this){response ->
            when (response){
                is ApiCallback.Success ->{
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")){
                        toast(mainObject.getString("message"))
                    }else{
                        toast(mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error ->{
                    progressDialog.dismiss()
                    toast(response.error)
                }
                is ApiCallback.Loading ->{
                    progressDialog.dismiss()
                    //toast("SIGNUP_RESPONSE_IS Loading false ")
                }
            }
        }
    }
}