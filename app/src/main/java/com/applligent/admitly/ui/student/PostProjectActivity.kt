package com.applligent.admitly.ui.student

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.R
import com.applligent.admitly.R.color.green
import com.applligent.admitly.adapter.CountrySpinnerAdapter
import com.applligent.admitly.databinding.ActivityPostProjectBinding
import com.applligent.admitly.databinding.DreamSchoolLayoutItemsBinding
import com.applligent.admitly.model.CountryModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.utils.preferences.getToken
import com.applligent.admitly.utils.toast
import com.applligent.admitly.viewmodel.PostProjectViewModel
import com.applligent.admitly.viewmodel.PostProjectViewModelFactory
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class PostProjectActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostProjectBinding
    var serviceId:Int = 1
    private var countryId: Int = 0
    lateinit var postProjectViewModel: PostProjectViewModel
    var countrySpinnerAdapter: CountrySpinnerAdapter? = null
    private lateinit var countryList: ArrayList<CountryModel?>
    private lateinit var progressDialog: ProgressDialog

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectButton()

        postProjectViewModel = ViewModelProvider(
            this,
            PostProjectViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(PostProjectViewModel::class.java)
        countryList = ArrayList<CountryModel?>()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")

        setListeners()
        setObserver()
        progressDialog.show()
        postProjectViewModel.getAllCountry()

    }
    private fun setListeners(){


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

        binding.addMoreDreamSchool.setOnClickListener {
            addView()
        }

        binding.postMyProject.setOnClickListener {
            if (isValidProjectDetails() == true){
                addProject()
            }
        }
    }
    private fun addView() {
        val bindingLayout = DreamSchoolLayoutItemsBinding.inflate(layoutInflater,null,false)
        bindingLayout.deleteTest.setOnClickListener {
            binding.testEmptyLayout1.removeView(bindingLayout.root)
        }
        binding.testEmptyLayout1.addView(bindingLayout.root)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectButton(){
        binding.smMatching.setOnClickListener {
            binding.smMatching.setImageResource(R.drawable.s_m_matching_select)
            binding.easyEditing.setImageResource(R.drawable.easy_editing)
            binding.textSM.setTextColor(resources.getColor(green,null))
            binding.textEE.setTextColor(resources.getColor(R.color.ocean_buble,null))
            serviceId = 1
        }
        binding.easyEditing.setOnClickListener {
            binding.easyEditing.setImageResource(R.drawable.easy_editing_select)
            binding.smMatching.setImageResource(R.drawable.s_m_matching)
            binding.textSM.setTextColor(resources.getColor(R.color.ocean_buble,null))
            binding.textEE.setTextColor(resources.getColor(green,null))
            serviceId = 2
        }
    }
    private fun isValidProjectDetails(): Boolean? {
        return if (binding.projectYearEt.text.toString().trim().isEmpty()) {
            binding.projectYearEt.requestFocus()
            binding.projectYearEt.error = "Enter year!"
            false
        } else if (binding.projectMajorEt.text.toString().trim().isEmpty()) {
            binding.projectMajorEt.requestFocus()
            binding.projectMajorEt.error = "Enter major!"
            false
        }else if (binding.projectDreamSchoolEt.text.toString().trim().isEmpty()) {
            binding.projectDreamSchoolEt.requestFocus()
            binding.projectDreamSchoolEt.error = "Enter school!"
            false
        } else {
            true
        }
    }
    private fun addProject(){
        progressDialog.show()
        val dreamSchoolArray = ArrayList<HashMap<String, Any>>()
        val mainMap = HashMap<String, Any>()
        mainMap.put("title",binding.projectDreamSchoolEt.text.toString())
        dreamSchoolArray.add(mainMap)

        for (k in 0 until binding.testEmptyLayout1.childCount) {
            val selectLayout = binding.testEmptyLayout1.getChildAt(k) as ConstraintLayout
            val sSchoolEt = selectLayout.findViewById<View>(R.id.row_dream_school_et) as EditText

            val hashMap = HashMap<String, Any>()
            hashMap.put("title",sSchoolEt.text.toString())
            dreamSchoolArray.add(hashMap)
        }
        val addProjectMap = HashMap<String, Any>()
        addProjectMap["countryId"] = countryId
        addProjectMap["year"] = binding.projectYearEt.text.toString().trim()
        addProjectMap["major"] = binding.projectMajorEt.text.toString().trim()
        addProjectMap["dreamSchools"] = dreamSchoolArray
        addProjectMap["serviceId"] = serviceId

        postProjectViewModel.addProject(addProjectMap,getToken())
    }
    private fun setObserver() {

        postProjectViewModel.projectCallback.observe(this
        ) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        val intent = Intent(this, StudentDashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        toast(mainObject.getString("message"))
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
                        //toast("SIGNUP_RESPONSE_IS Loading false ")
                    }
                }
            }
        }

        postProjectViewModel.countryCallback.observe(this
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
                                CountrySpinnerAdapter(this, countryList)
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