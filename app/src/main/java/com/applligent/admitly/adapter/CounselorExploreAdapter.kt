package com.applligent.admitly.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applligent.admitly.databinding.ItemCounselorExploreLayoutBinding
import com.applligent.admitly.model.CounselorExploreModel
import com.applligent.admitly.ui.counselor.ProposalToStudentActivity


class CounselorExploreAdapter(var context:Context ,private var exploreList: ArrayList<CounselorExploreModel>) : RecyclerView.Adapter<CounselorExploreAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(ItemCounselorExploreLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = exploreList[position]
        holder.binding.apply {
            this.tvCountry.text = item.countryName
            this.proposeBtn.setOnClickListener {
                val intent =  Intent(context,ProposalToStudentActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("projectId",item.projectId)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = exploreList.size
    fun submitList(emptyList: ArrayList<CounselorExploreModel>) {
        exploreList = emptyList
    }

    class MyViewHolder(val binding: ItemCounselorExploreLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}