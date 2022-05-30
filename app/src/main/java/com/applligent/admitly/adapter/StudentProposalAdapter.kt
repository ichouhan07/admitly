package com.applligent.admitly.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applligent.admitly.databinding.ItemStudentProposalLayoutBinding
import com.applligent.admitly.model.StudentExploreModel
import com.applligent.admitly.model.StudentProposalModel
import com.applligent.admitly.ui.student.HireCounselorProfileActivity

class StudentProposalAdapter(var context: Context, private var proposalList: ArrayList<StudentProposalModel>): RecyclerView.Adapter<StudentProposalAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(ItemStudentProposalLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = proposalList[position]
        holder.binding.apply {
            this.tvCounselorCountry.text = item.countryId.toString()
            this.hireBtn.setOnClickListener {
                val intent = Intent(context, HireCounselorProfileActivity::class.java)
                intent.putExtra("counsellorId",item.counsellorId)
                intent.putExtra("projectId",item.projectId)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
    override fun getItemCount(): Int = proposalList.size
    fun submitList(emptyList: ArrayList<StudentProposalModel>) {
        proposalList = emptyList
    }
    class MyViewHolder(val binding: ItemStudentProposalLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}