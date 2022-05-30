package com.applligent.admitly.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applligent.admitly.databinding.ItemStudentExploreLayoutBinding
import com.applligent.admitly.model.StudentExploreModel
import com.applligent.admitly.ui.student.HireCounselorProfileActivity
import com.applligent.admitly.ui.student.ReviewCounselorActivity

class StudentExploreAdapter( var context:Context, private var exploreList: ArrayList<StudentExploreModel>) : RecyclerView.Adapter<StudentExploreAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(ItemStudentExploreLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = exploreList[position]
        holder.binding.apply {
            //Glide.with(Activity()).load(item.profileURL).into(this.ivProfileImg)
            this.tvCounselorName.text = item.name
            this.tvCounselorCountry.text = item.countryName
            //this.tvRatings.text = item.rating.toString()
            this.hireBtn.setOnClickListener {
                val intent = Intent(context, ReviewCounselorActivity::class.java)
                //intent.putExtra("counsellorId", item.services[0].counsellorId)
                intent.putExtra("userId",item.userId)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
    override fun getItemCount(): Int = exploreList.size
    fun submitList(emptyList: ArrayList<StudentExploreModel>) {
        exploreList = emptyList
    }

    class MyViewHolder(val binding: ItemStudentExploreLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}