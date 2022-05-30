package com.applligent.admitly.ui.student.studentfragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applligent.admitly.chatSystem.adapter.ChatListAdapter
import com.applligent.admitly.chatSystem.model.Users
import com.applligent.admitly.databinding.FragmentStuMessageBinding
import com.applligent.admitly.ui.comman.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StuMessageFragment : Fragment() {
    lateinit var binding: FragmentStuMessageBinding
    lateinit var progressDialog: ProgressDialog
    private lateinit var chatListAdapter: ChatListAdapter
    private lateinit var usersArrayList: ArrayList<Users>

    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var dataSnapshot: DataSnapshot

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStuMessageBinding.inflate(inflater,container,false)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Please Wait")
        progressDialog.setCancelable(false)
        progressDialog.show()

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")
        usersArrayList = ArrayList<Users>()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children){
                    val users = dataSnapshot.getValue(Users::class.java)
                    usersArrayList.add(users as Users)
                    Log.i("TAG", "onDataChange: wesadsd$users")
                }
                chatListAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        chatListAdapter = ChatListAdapter(activity,usersArrayList)
        binding.chatListRv.layoutManager = LinearLayoutManager(activity)
        binding.chatListRv.adapter = chatListAdapter

        /*if(auth.currentUser == null)
        {
            val intent = Intent(activity, SignUpActivity::class.java)
            activity?.startActivity(intent)
        }*/

        return binding.root
    }

}