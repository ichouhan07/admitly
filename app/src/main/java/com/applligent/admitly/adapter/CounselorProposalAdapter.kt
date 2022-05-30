package com.applligent.admitly.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applligent.admitly.databinding.ItemCounselorProposalLayoutBinding
import com.applligent.admitly.model.CounselorExploreModel
import com.applligent.admitly.model.CounselorProposalModel

class CounselorProposalAdapter(private var proposalList: ArrayList<CounselorProposalModel>): RecyclerView.Adapter<CounselorProposalAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(ItemCounselorProposalLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = proposalList[position]
        holder.binding.apply {
            this.tvCreated.text = item.created
        }
    }
    override fun getItemCount(): Int = proposalList.size
    fun submitList(emptyList: ArrayList<CounselorProposalModel>) {
        proposalList = emptyList
    }
    class MyViewHolder(val binding: ItemCounselorProposalLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}