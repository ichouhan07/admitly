package com.applligent.admitly.ui.counselor.counselorfragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.applligent.admitly.adapter.CounselorExploreAdapter
import com.applligent.admitly.adapter.CountrySpinnerAdapter
import com.applligent.admitly.adapter.ServiceSpinnerAdapter
import com.applligent.admitly.databinding.FragmentConExploreBinding
import com.applligent.admitly.databinding.ProjectsFilterBottomDialogueBinding
import com.applligent.admitly.model.CounselorExploreModel
import com.applligent.admitly.model.CountryModel
import com.applligent.admitly.model.ServiceModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.ui.counselor.MyServiceActivity
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.utils.log
import com.applligent.admitly.viewmodel.CounselorDBViewModel
import com.applligent.admitly.viewmodel.CounselorDBViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class ConExploreFragment : Fragment() {
    lateinit var binding: FragmentConExploreBinding
    private lateinit var exploreList: ArrayList<CounselorExploreModel>
    lateinit var exploreAdapter: CounselorExploreAdapter
    lateinit var cdbViewModel: CounselorDBViewModel
    private lateinit var progressDialog: ProgressDialog

    //bottomDialogue
    private var countryId: Int = 0
    var countrySpinnerAdapter: CountrySpinnerAdapter? = null
    private lateinit var countryList: ArrayList<CountryModel?>
    private var serviceId: Int = 0
    var serviceSpinnerAdapter: ServiceSpinnerAdapter? = null
    private lateinit var serviceList: ArrayList<ServiceModel?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConExploreBinding.inflate(inflater,container,false)

        cdbViewModel = ViewModelProvider(
            viewModelStore,
            CounselorDBViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(CounselorDBViewModel::class.java)

        setResources()
        setListener()
        setObserver()
        progressDialog.show()
        cdbViewModel.getAllCountry()
        cdbViewModel.getAllService()

        return binding.root
    }

    private fun setResources() {
        exploreList = ArrayList<CounselorExploreModel>()
        countryList = ArrayList<CountryModel?>()
        serviceList = ArrayList<ServiceModel?>()
        exploreAdapter =  CounselorExploreAdapter(requireContext(),exploreList)
        binding.counselorExploreRecycle.adapter = exploreAdapter
        serviceSpinnerAdapter = ServiceSpinnerAdapter(requireContext(), serviceList)
        countrySpinnerAdapter = CountrySpinnerAdapter(requireContext(), countryList)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")
    }
    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        binding.swipeRefreshLayout.isRefreshing = true
        val exploreMap = HashMap<String, Any>()
        exploreMap["serviceId"] = 0
        exploreMap["countryId"] = 0
        cdbViewModel.getUserProjects(exploreMap)
    }
    private fun setListener() {

        binding.ivCreateService.setOnClickListener {
            val intent = Intent(requireContext(),MyServiceActivity::class.java)
            startActivity(intent)
        }

        binding.filter.setOnClickListener { showBottomSheetDialog() }

        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener);

        val exploreMap = HashMap<String, Any>()
        exploreMap["serviceId"] = 0
        exploreMap["countryId"] = 0
        cdbViewModel.getUserProjects(exploreMap)
    }

    private fun showBottomSheetDialog() {
        val bindingLayout = ProjectsFilterBottomDialogueBinding.inflate(layoutInflater,null,false)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bindingLayout.root)
        bottomSheetDialog.show()
        bindingLayout.dismissBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bindingLayout.countrySpinner.adapter = countrySpinnerAdapter
        bindingLayout.countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    countryId = countryList[position]?.id!!
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        bindingLayout.serviceSpinner.adapter = serviceSpinnerAdapter
        bindingLayout.serviceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    serviceId = serviceList[position]?.serviceId!!
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        bindingLayout.applyBtn.setOnClickListener {
            progressDialog.show()
            val exploreMap = HashMap<String, Any>()
            exploreMap["countryId"] = countryId
            exploreMap["serviceId"] = serviceId
            cdbViewModel.getUserProjects(exploreMap)
            bottomSheetDialog.dismiss()
            log("fbjsbi"+exploreMap)
        }
    }

    private fun setObserver() {
        cdbViewModel.exploreCallback.observe(requireActivity()) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        try {
                            val jsonArray = mainObject.getJSONArray("data")
                            exploreList.clear()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val exploreModel = Gson().fromJson(jsonObject.toString(), CounselorExploreModel::class.java)
                                exploreList.add(exploreModel)
                            }
                            exploreAdapter.submitList(exploreList)
                            binding.tvMessage.visibility = View.GONE
                            exploreAdapter.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            progressDialog.dismiss()
                            e.printStackTrace()
                        }
                    } else {
                        exploreAdapter.submitList(arrayListOf<CounselorExploreModel>())
                        exploreAdapter.notifyDataSetChanged()
                        binding.tvMessage.text = mainObject.getString("message")
                        binding.tvMessage.visibility = View.VISIBLE
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
                    progressDialog.dismiss()
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    Comman.showLongToast(requireContext(), response.error)
                }
                is ApiCallback.Loading -> {
                    if (!response.isLoading) {
                        progressDialog.dismiss()
                        println("SIGNUP_RESPONSE_IS Loading false ")
                    }
                }
            }
        }
        cdbViewModel.countryCallback.observe(requireActivity()) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        try {
                            val jsonArray = mainObject.getJSONArray("data");
                            countryList.clear()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val countryModel = CountryModel(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("iso3")
                                )
                                countryList.add(countryModel)
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        Comman.showLongToast(requireContext(), mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    Comman.showLongToast(requireContext(), response.error)
                }
                is ApiCallback.Loading -> {
                    if (!response.isLoading) {
                        progressDialog.dismiss()
                        println("COUNTRY Loading false ")
                    }
                }
            }
        }

        cdbViewModel.serviceCallback.observe(requireActivity()) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        try {
                            val jsonArray = mainObject.getJSONArray("data");
                            serviceList.clear()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val serviceModel = ServiceModel(
                                    jsonObject.getInt("serviceId"),
                                    jsonObject.getString("title"),
                                    jsonObject.getInt("status")
                                )
                                serviceList.add(serviceModel)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        Comman.showLongToast(requireContext(), mainObject.getString("message"))
                    }
                }
                is ApiCallback.Error -> {
                    progressDialog.dismiss()
                    Comman.showLongToast(requireContext(), response.error)
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