package com.applligent.admitly.ui.student

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import androidx.lifecycle.ViewModelProvider
import com.applligent.admitly.databinding.ActivityReviewCounselorBinding
import com.applligent.admitly.network.ApiCallback
import com.applligent.admitly.network.ApiClient
import com.applligent.admitly.network.ApiInterface
import com.applligent.admitly.ui.comman.Repository
import com.applligent.admitly.utils.Comman
import com.applligent.admitly.utils.log
import com.applligent.admitly.utils.toast
import com.applligent.admitly.viewmodel.ReviewCounselorViewModel
import com.applligent.admitly.viewmodel.ReviewCounselorViewModelFactory
import com.google.gson.Gson
import org.json.JSONObject

class ReviewCounselorActivity : AppCompatActivity(){
    lateinit var binding: ActivityReviewCounselorBinding
    private  var counsellorId:Int = 0
    lateinit var reviewViewModel: ReviewCounselorViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewCounselorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //counsellorId from Intent
        counsellorId = intent.getIntExtra("userId",0)

        //ratingBars
        binding.ratingBar1.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, ratingValue, _ ->
            binding.tvRatingCompetency.text =  "$ratingValue"
        }
        binding.ratingBar2.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, ratingValue, _ ->
            binding.tvRatingProfessionalism.text =  "$ratingValue"
        }
        binding.ratingBar3.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, ratingValue, _ ->
            binding.tvRatingResponsiveness.text = "$ratingValue"
        }

        //viewModel instance
        reviewViewModel = ViewModelProvider(
            this,
            ReviewCounselorViewModelFactory(
                Repository(
                    ApiClient().getClient()!!.create(ApiInterface::class.java)
                )
            )
        ).get(ReviewCounselorViewModel::class.java)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait")
        setListener()
        setObserver()
    }
    private fun setListener() {
        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.sendReviewBtn.setOnClickListener {
            //for testing token
            val token: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI0IiwiZW1haWwiOiJqYW1lc0BqZC5jb20iLCJ1c2VyVHlwZSI6IjIifQ.zsosfSfAgnu5JG3kQ31_1E0bC3j1H4XEExr1HtO1FgY"
            val reviewMap = HashMap<String, Any>()
            reviewMap["counsellorId"] = counsellorId
            reviewMap["competency"] = binding.tvRatingCompetency.text.toString()
            reviewMap["responsiveness"] = binding.tvRatingProfessionalism.text.toString()
            reviewMap["professionalism"] = binding.tvRatingResponsiveness.text.toString()
            reviewMap["feedback"] = binding.feedbackEt.text.toString().trim()
            //log("guyjgkh$reviewMap")
            reviewViewModel.getReviewCounselor(reviewMap,token)
        }
    }
    private fun setObserver() {
        reviewViewModel.reviewCallback.observe(this) { response ->
            when (response) {
                is ApiCallback.Success -> {
                    progressDialog.dismiss()
                    val res = Gson().toJson(response.data)
                    val mainObject = JSONObject(res)
                    if (mainObject.getBoolean("success")) {
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
    }
}