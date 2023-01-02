package com.applligent.admitly.ui.student.studentfragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.adapter.ServiceSpinnerAdapter
import com.applligent.admitly.adapter.StudentProjectAdapter
import com.applligent.admitly.adapter.StudentProposalAdapter
import com.applligent.admitly.databinding.FragmentStuProjectBinding
import com.applligent.admitly.dialog.DialogFilterProject
import com.applligent.admitly.model.ServiceModel
import com.applligent.admitly.model.StudentProjectModel
import com.applligent.admitly.model.StudentProposalModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.viewmodel.StudentDBViewModel
import com.applligent.admitly.viewmodel.StudentDBViewModelFactory
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class StuProjectFragment : Fragment() {
    lateinit var binding: FragmentStuProjectBinding
    lateinit var sdbViewModel: StudentDBViewModel
    private lateinit var projectList: ArrayList<StudentProjectModel>
    private lateinit var proposalList: ArrayList<StudentProposalModel>
    lateinit var projectAdapter: StudentProjectAdapter
    lateinit var proposalAdapter: StudentProposalAdapter
    private var serviceId: Int = -1
    var serviceSpinnerAdapter: ServiceSpinnerAdapter? = null
    private lateinit var serviceList: ArrayList<ServiceModel?>
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentStuProjectBinding.inflate(inflater,container,false)
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
        sdbViewModel.getAllService()
        return binding.root
    }

    private fun setResources() {
        projectList = ArrayList<StudentProjectModel>()
        serviceList = ArrayList<ServiceModel?>()
        proposalList = ArrayList<StudentProposalModel>()
        projectAdapter = StudentProjectAdapter(requireContext(),projectList)
        serviceSpinnerAdapter = ServiceSpinnerAdapter(requireContext(), serviceList)
        proposalAdapter = StudentProposalAdapter(requireContext(), proposalList)
        binding.studentProposalRecycle.adapter = proposalAdapter
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")
    }

    private fun setListener() {
        //for testing token
        val token1: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIzIiwiZW1haWwiOiJ0ZXN0MTExQHRlc3QuY29tIiwidXNlclR5cGUiOiIxIn0.vDqOR7KLdO1bIcBKMVIPiUzttW0dejYsYzickWPd6do"
        binding.filter.setOnClickListener {
            val dialogFilterProject = DialogFilterProject(requireActivity())
            dialogFilterProject.show()
            dialogFilterProject.applyFilterOnCurrentProject = {
                val projectMap = HashMap<String, Any>()
                projectMap["status"] = 1
                sdbViewModel.getStudentProject(projectMap,token1)
            }
            dialogFilterProject.applyFilterOnPastProject = {
                val projectMap = HashMap<String, Any>()
                projectMap["status"] = 2
                sdbViewModel.getStudentProject(projectMap,token1)
            }
        }
        val projectMap = HashMap<String, Any>()
        projectMap["status"] = 0
        sdbViewModel.getStudentProject(projectMap,token1)

        binding.serviceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    serviceId = serviceList[position]?.serviceId!!
                    val proposalMap = HashMap<String, Any>()
                    proposalMap["serviceId"] = serviceId
                    //log("bjbju" +proposalMap)
                    sdbViewModel.getStudentProposal(proposalMap,token1)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

    }

    private fun setObserver() {
        sdbViewModel.projectCallback.observe(requireActivity()) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        try {
                            val jsonArray = mainObject.getJSONArray("data")
                            projectList.clear()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val projectModel = Gson().fromJson(jsonObject.toString(), StudentProjectModel::class.java)
                                projectList.add(projectModel)
                            }
                            binding.studentProjectRecycle.adapter = projectAdapter
                            projectAdapter.notifyDataSetChanged()
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
                        println("SIGNUP_RESPONSE_IS Loading false ")
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

                            binding.serviceSpinner.adapter = serviceSpinnerAdapter
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
                        System.out.println("COUNTRY Loading false ")
                    }
                }
            }
        }
        sdbViewModel.proposalCallback.observe(requireActivity()) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        try {
                            val jsonArray = mainObject.getJSONArray("data")
                            proposalList.clear()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val proposalModel = Gson().fromJson(jsonObject.toString(),StudentProposalModel::class.java)
                                proposalList.add(proposalModel)
                            }
                            proposalAdapter.submitList(proposalList)
                            binding.tvMessage.visibility = View.GONE
                            proposalAdapter.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        proposalAdapter.submitList(arrayListOf<StudentProposalModel>())
                        proposalAdapter.notifyDataSetChanged()
                        binding.tvMessage.text = mainObject.getString("message")
                        binding.tvMessage.visibility = View.VISIBLE
                    }
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
    }
}