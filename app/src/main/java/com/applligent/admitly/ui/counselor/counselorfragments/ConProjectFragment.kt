package com.applligent.admitly.ui.counselor.counselorfragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.applligent.admitly.R
import com.applligent.admitly.adapter.CounselorProposalAdapter
import com.applligent.admitly.databinding.FragmentConProjectBinding
import com.applligent.admitly.model.CounselorExploreModel
import com.applligent.admitly.model.CounselorProposalModel
import com.applligent.admitly.model.CountryModel
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.viewmodel.CounselorDBViewModel
import com.applligent.admitly.viewmodel.CounselorDBViewModelFactory
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject


class ConProjectFragment : Fragment() {
    lateinit var binding: FragmentConProjectBinding
    private lateinit var proposalList: ArrayList<CounselorProposalModel>
    lateinit var proposalAdapter: CounselorProposalAdapter
    lateinit var cdbViewModel: CounselorDBViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConProjectBinding.inflate(inflater,container,false)
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

        //for example token
        val token: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI3NCIsImVtYWlsIjoidGVzdGNvdW5zZWxvcjFAdGVzdC5jb20iLCJ1c2VyVHlwZSI6IjIifQ.83x7NjuJ3UAi215BNTm-Go0npVdX5NyaivwW4vY-6wA"

        cdbViewModel.getProposalAll(token)
        return binding.root
    }

    private fun setResources() {
        proposalList = ArrayList<CounselorProposalModel>()
        proposalAdapter = CounselorProposalAdapter(proposalList)
        binding.counselorProposalRecycle.adapter = proposalAdapter
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")
    }
    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        binding.swipeRefreshLayout.isRefreshing = true
        //for example token
        val token: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI3NCIsImVtYWlsIjoidGVzdGNvdW5zZWxvcjFAdGVzdC5jb20iLCJ1c2VyVHlwZSI6IjIifQ.83x7NjuJ3UAi215BNTm-Go0npVdX5NyaivwW4vY-6wA"
        cdbViewModel.getProposalAll(token)
    }
    private fun setListener() {
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener);
    }
    private fun setObserver() {
        cdbViewModel.proposalCallback.observe(requireActivity()) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
                        try {
                            val jsonArray = mainObject.getJSONArray("data");
                            proposalList.clear()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val proposalModel = Gson().fromJson(jsonObject.toString(), CounselorProposalModel::class.java)
                                proposalList.add(proposalModel)
                            }
                            proposalAdapter.submitList(proposalList)
                            proposalAdapter.notifyDataSetChanged()

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