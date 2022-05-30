package com.applligent.admitly.ui.counselor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.R
import com.applligent.admitly.adapter.ServiceSpinnerAdapter
import com.applligent.admitly.databinding.ActivityMyServiceBinding
import com.applligent.admitly.databinding.MyServiceLayoutItemsBinding
import com.applligent.admitly.model.ServiceModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.ui.student.StudentDashboardActivity
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.utils.log
import com.applligent.admitly.utils.preferences.getToken
import com.applligent.admitly.utils.toast
import com.applligent.admitly.viewmodel.MyServiceViewModel
import com.applligent.admitly.viewmodel.MyServiceViewModelFactory
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class MyServiceActivity : AppCompatActivity() {
    lateinit var binding:ActivityMyServiceBinding
    private var serviceId: Int = 0
    lateinit var myServiceViewModel: MyServiceViewModel
    var serviceSpinnerAdapter: ServiceSpinnerAdapter? = null
    private lateinit var serviceList: ArrayList<ServiceModel?>
    private lateinit var progressDialog: ProgressDialog

    //hourly/fixed
    var serviceArrayAdapter: ArrayAdapter<*>? = null
    private var service = arrayOf("Hourly", "Fixed")
    private var priceType: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myServiceViewModel = ViewModelProvider(
            this,
            MyServiceViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(MyServiceViewModel::class.java)
        serviceList = ArrayList<ServiceModel?>()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")

        setListeners()
        setObserver()
        progressDialog.show()
        myServiceViewModel.getAllService()
    }

    private fun setListeners(){

        //skip example
        binding.skip.setOnClickListener { startActivity(Intent(this,CounselorDashboardActivity::class.java)) }

        binding.serviceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    serviceId = serviceList[position]?.serviceId!!
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        binding.serviceBack.setOnClickListener { onBackPressed() }

        binding.addMoreService.setOnClickListener {
            addView()
        }
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

        binding.finishBtn.setOnClickListener {
            if (isValidServiceDetails() == true){
                addService()
            }
        }
    }
    private fun addView() {
        val titles = serviceList.map { it?.title }
         val bindingLayout = MyServiceLayoutItemsBinding.inflate(layoutInflater,null,false)

        bindingLayout.rowServiceSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titles)
        bindingLayout.rowServiceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    serviceList.forEach {
                        if (it?.title == titles[position]){
                            if (it != null) {
                                bindingLayout.serviceId.text = it.serviceId.toString()
                            }
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        bindingLayout.rowHourlyFixedSpinner.adapter = serviceArrayAdapter
            bindingLayout.rowHourlyFixedSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedPrice = service[position]
                    priceType = if (selectedPrice == service[0]){
                         1
                    }else{
                        2
                    }
                    bindingLayout.priceType.text = priceType.toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        bindingLayout.deleteTest.setOnClickListener {
            binding.testEmptyLayout2.removeView(bindingLayout.root)
        }
        binding.testEmptyLayout2.addView(bindingLayout.root)
    }
    private fun isValidServiceDetails(): Boolean {
        return if (binding.yearOfExpEt.text.toString().trim().isEmpty()) {
            binding.yearOfExpEt.requestFocus()
            binding.yearOfExpEt.error = "Enter experience!"
            false
        } else if (binding.priceAmountEt.text.toString().trim().isEmpty()) {
            binding.priceAmountEt.requestFocus()
            binding.priceAmountEt.error = "Enter price!"
            false
        } else {
            true
        }
    }
    private fun addService() {
        progressDialog.show()
        val serviceArray = java.util.ArrayList<HashMap<String, Any>>()
        val mainMap = HashMap<String, Any>()
        mainMap["serviceId"] = serviceId
        mainMap["experience"] = binding.yearOfExpEt.text.toString().trim()
        mainMap["price"] = binding.priceAmountEt.text.toString().trim()
        mainMap["priceType"] = priceType
        serviceArray.add(mainMap)

        for (k in 0 until binding.testEmptyLayout2.childCount) {
            val selectLayout = binding.testEmptyLayout2.getChildAt(k) as ConstraintLayout
            serviceId = selectLayout.findViewById<TextView>(R.id.service_id).text.toString().toInt()
            val yearOfExpEt = selectLayout.findViewById<View>(R.id.row_year_of_exp_et) as EditText
            val priceAmount = selectLayout.findViewById<View>(R.id.row_price_amount_et) as EditText
            priceType = selectLayout.findViewById<TextView>(R.id.price_type).text.toString().toInt()

            val hashMap = HashMap<String, Any>()
            hashMap["serviceId"] = serviceId
            hashMap["experience"] = yearOfExpEt.text.toString()
            hashMap["price"] = priceAmount.text.toString()
            hashMap["priceType"] = priceType
            serviceArray.add(hashMap)
        }
//        log("GUGHIIHIH"+serviceArray)
        val addServiceMap = HashMap<String, Any>()
        addServiceMap["services"] = serviceArray
        myServiceViewModel.addService(addServiceMap,getToken())
    }
    private fun setObserver() {

        myServiceViewModel.myServiceCallback.observe(this
        ) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        val intent = Intent(this, CounselorDashboardActivity::class.java)
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
                        log("SIGNUP_RESPONSE_IS Loading false ")
                    }
                }
            }
        }

        myServiceViewModel.serviceCallback.observe(this
        ) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        try {
                            val jsonArray = mainObject.getJSONArray("data");
                            serviceList.clear()
                            for (i in 0..jsonArray.length() - 1) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val serviceModel = ServiceModel(
                                    jsonObject.getInt("serviceId"),
                                    jsonObject.getString("title"),
                                    jsonObject.getInt("status")
                                )
                                serviceList.add(serviceModel)
                            }
                            serviceSpinnerAdapter =
                                ServiceSpinnerAdapter(this, serviceList)
                            binding.serviceSpinner.adapter = serviceSpinnerAdapter
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