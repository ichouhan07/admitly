package com.applligent.admitly.ui.counselor

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.R
import com.applligent.admitly.databinding.ActivityProposalToStudentBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.utils.log
import com.applligent.admitly.utils.toast
import com.applligent.admitly.viewmodel.CounselorDBViewModel
import com.applligent.admitly.viewmodel.CounselorDBViewModelFactory
import com.applligent.admitly.viewmodel.MyServiceViewModel
import com.applligent.admitly.viewmodel.MyServiceViewModelFactory
import com.google.gson.Gson
import org.json.JSONObject

class ProposalToStudentActivity : AppCompatActivity() {
    lateinit var binding: ActivityProposalToStudentBinding
    private  var projectId:Int = 0
    lateinit var cdbViewModel: CounselorDBViewModel
    private lateinit var progressDialog: ProgressDialog

    //hourly/fixed
    var serviceArrayAdapter: ArrayAdapter<*>? = null
    private var service = arrayOf("Hourly", "Fixed")
    private var priceType: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProposalToStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        projectId = intent.getIntExtra("projectId",0)

        cdbViewModel = ViewModelProvider(
            this,
            CounselorDBViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(CounselorDBViewModel::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")

        setListener()
        setObserver()

    }


    private fun setListener() {
        binding.backBtn1.setOnClickListener { onBackPressed() }
        serviceArrayAdapter = ArrayAdapter<Any?>(this, R.layout.spinner_item, service)
        binding.hourlyFixedSpinner.adapter = serviceArrayAdapter
        binding.hourlyFixedSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedPrice = service[position]
                    priceType = if (selectedPrice == service[0]){
                        1
                    }else{
                        2
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        binding.submitBtn.setOnClickListener {
            if (isValidSignUpDetails() == true){
                //for texting token
                val token: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI3NCIsImVtYWlsIjoidGVzdGNvdW5zZWxvcjFAdGVzdC5jb20iLCJ1c2VyVHlwZSI6IjIifQ.83x7NjuJ3UAi215BNTm-Go0npVdX5NyaivwW4vY-6wA"
                val proposeMap = HashMap<String, Any>()
                proposeMap["projectId"] = projectId
                proposeMap["priceType"] = priceType
                proposeMap["price"] = binding.priceAmountEt.text.toString().trim()
                proposeMap["coverLetter"] = binding.coverLetterEt.text.toString().trim()
                //log("gjgfjd"+proposeMap)
                cdbViewModel.getStudentProposal(proposeMap,token)
            }
        }
    }
    private fun isValidSignUpDetails(): Boolean? {
        return if (binding.priceAmountEt.text.toString().trim().isEmpty()) {
            binding.priceAmountEt.requestFocus()
            binding.priceAmountEt.error = "Enter price!"
            false
        }else if (binding.coverLetterEt.text.toString().trim().isEmpty()){
            binding.coverLetterEt.requestFocus()
            binding.coverLetterEt.error = "Write something!"
            false
        }else{
            true
        }
    }
    private fun setObserver() {
        cdbViewModel.studentProposalCallback.observe(this
        ) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        toast(mainObject.getString("message"))
                        binding.priceAmountEt.text.clear()
                        binding.coverLetterEt.text.clear()
                    } else {
                        toast(mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    Comman.showLongToast(this, response.error)
                }
                is ApiCallback.Loading -> {
                    if (!response.isLoading) {
                        progressDialog.dismiss()
                        log("SIGNUP_RESPONSE_IS Loading false ")
                    }
                }
            }
        }
    }

}