package com.applligent.admitly.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.applligent.admitly.databinding.ItemStudentProjectsLayoutBinding
import com.applligent.admitly.dialog.DialogSuccessfullyRelease
import com.applligent.admitly.dialog.DialogSureReleasePayment
import com.applligent.admitly.model.StudentProjectModel

class StudentProjectAdapter(private var conntext: Context, private var projectList: ArrayList<StudentProjectModel>) : RecyclerView.Adapter<StudentProjectAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(ItemStudentProjectsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = projectList[position]
        holder.binding.apply {
            this.tvCreated.text = item.created
            this.rowReleasePaymentBtn.setOnClickListener {
                val dialogSureReleasePayment = DialogSureReleasePayment(conntext as Activity)
                dialogSureReleasePayment.show()
                dialogSureReleasePayment.confirmReleaseButton = {
                    val dialogSuccessfullyReleased = DialogSuccessfullyRelease(conntext as Activity)
                    dialogSuccessfullyReleased.show()
                    dialogSureReleasePayment.dismiss()
                }
            }
        }
    }

    override fun getItemCount(): Int = projectList.size
    class MyViewHolder(val binding: ItemStudentProjectsLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}