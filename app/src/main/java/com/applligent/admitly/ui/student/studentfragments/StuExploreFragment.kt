package com.applligent.admitly.ui.student.studentfragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.applligent.admitly.adapter.CountrySpinnerAdapter
import com.applligent.admitly.adapter.ServiceSpinnerAdapter
import com.applligent.admitly.adapter.StudentExploreAdapter
import com.applligent.admitly.databinding.CounselorFilterBottomDialogueBinding
import com.applligent.admitly.databinding.FragmentStuExploreBinding
import com.applligent.admitly.model.CountryModel
import com.applligent.admitly.model.ServiceModel
import com.applligent.admitly.model.StudentExploreModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.ui.student.PostProjectActivity
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.viewmodel.StudentDBViewModel
import com.applligent.admitly.viewmodel.StudentDBViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class StuExploreFragment : Fragment() {
    lateinit var binding: FragmentStuExploreBinding
    private lateinit var exploreList: ArrayList<StudentExploreModel>
    lateinit var exploreAdapter: StudentExploreAdapter
    lateinit var sdbViewModel: StudentDBViewModel
    private lateinit var progressDialog: ProgressDialog

    //bottomDialogue
    private var countryId: Int = 0
    var countrySpinnerAdapter: CountrySpinnerAdapter? = null
    private lateinit var countryList: ArrayList<CountryModel?>
    private var serviceId: Int = 0
    var serviceSpinnerAdapter: ServiceSpinnerAdapter? = null
    private lateinit var serviceList: ArrayList<ServiceModel?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStuExploreBinding.inflate(inflater,container,false)

        sdbViewModel = ViewModelProvider(
            viewModelStore,
            StudentDBViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(StudentDBViewModel::class.java)
        setResources()
        setListener()
        setObserver()
        progressDialog.show()
        sdbViewModel.getAllCountry()
        sdbViewModel.getAllService()

        return binding.root
    }
    private fun setResources() {
        exploreList = ArrayList<StudentExploreModel>()
        countryList = ArrayList<CountryModel?>()
        serviceList = ArrayList<ServiceModel?>()
        exploreAdapter = StudentExploreAdapter(requireContext(),exploreList)
        binding.studentExploreRecycle.adapter = exploreAdapter
        serviceSpinnerAdapter = ServiceSpinnerAdapter(requireContext(), serviceList)
        countrySpinnerAdapter = CountrySpinnerAdapter(requireContext(), countryList)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")
    }
    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        binding.swipeRefreshLayout.isRefreshing = true
        val exploreMap = HashMap<String, Any>()
        exploreMap["countryId"] = 0
        exploreMap["serviceId"] = 0
        exploreMap["experience"] = 0
        sdbViewModel.getExploreCounselor(exploreMap)
    }
    private fun setListener() {
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)

        binding.ivCreateProject.setOnClickListener {
            val intent = Intent(requireContext(), PostProjectActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.filter.setOnClickListener { showBottomSheetDialog() }

        val exploreMap = HashMap<String, Any>()
        exploreMap["countryId"] = 0
        exploreMap["serviceId"] = 0
        exploreMap["experience"] = 0
        sdbViewModel.getExploreCounselor(exploreMap)
    }

    private fun showBottomSheetDialog() {
        val bindingLayout = CounselorFilterBottomDialogueBinding.inflate(layoutInflater,null,false)
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
                exploreMap["experience"] = bindingLayout.etExperience.text.toString().trim()
                sdbViewModel.getExploreCounselor(exploreMap)
                bottomSheetDialog.dismiss()
                //log("fbjbi"+exploreMap)
            }
        }
    private fun setObserver() {
        sdbViewModel.exploreCounselorCallback.observe(requireActivity()) { response ->
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
                                val exploreModel = Gson().fromJson(jsonObject.toString(),StudentExploreModel::class.java)
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
                        exploreAdapter.submitList(arrayListOf<StudentExploreModel>())
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
        sdbViewModel.countryCallback.observe(requireActivity()) { response ->
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

        sdbViewModel.serviceCallback.observe(requireActivity()) { response ->
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


/*
class MyDialogFragment : BottomSheetDialogFragment() {
    override fun getTheme() = R.style.NoBackgroundDialogTheme
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = View.inflate(requireContext(), R.layout.counselor_filter_bottom_dialogue, null)
        view.setBackgroundResource(R.drawable.dialogue_box_radius)
        val  btn = view.findViewById<ImageView>(R.id.dismiss_btn)

        return view
    }
}*/
