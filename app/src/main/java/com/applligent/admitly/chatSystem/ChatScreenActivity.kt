package com.applligent.admitly.chatSystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.applligent.admitly.chatSystem.adapter.MessageAdapter
import com.applligent.admitly.chatSystem.model.MessageModel
import com.applligent.admitly.databinding.ActivityChatScreenBinding
import com.applligent.admitly.utils.preferences.getUserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatScreenBinding
    lateinit var recieverName: String
    lateinit var SenderUid: String
    lateinit var recieverUid: String
    lateinit var senderRoom: String
    lateinit var reciverRoom: String

    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var ChatReference: DatabaseReference

    private lateinit var messagesArrayList: ArrayList<MessageModel>
    lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messagesArrayList = ArrayList<MessageModel>()

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        SenderUid = getUserId().toString()

        recieverName = intent.getStringExtra("reciver_name").toString()
        recieverUid = intent.getStringExtra("userId").toString()
        binding.name.text = recieverName

        Log.i("TAG", "onCreate: rwererd SID = "+SenderUid+"RID = "+recieverUid)

        senderRoom = SenderUid+recieverUid
        reciverRoom = recieverUid+SenderUid

//        ChatRecyclerView
        val reverseLayout = true
        messageAdapter = MessageAdapter(this,messagesArrayList)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        binding.ChattingRv.layoutManager = linearLayoutManager
        binding.ChattingRv.adapter = messageAdapter

        reference = firebaseDatabase.getReference().child("user").child(SenderUid)
        ChatReference = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages")
        ChatReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                messagesArrayList.clear()
                for (dataSnapshot in snapshot.children) {
                    val msgPojo = dataSnapshot.getValue(MessageModel::class.java)
                    if (msgPojo != null) {
                        messagesArrayList.add(msgPojo)
                        binding.ChattingRv.smoothScrollToPosition(messagesArrayList.count())
                    }
                    messageAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        listeners()

    }

    fun listeners(){

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.messageSendBtn.setOnClickListener {
            val message: String = binding.messageEditText.getText().toString()
            if (message.isEmpty())
            {
                Toast.makeText(applicationContext, "Empty Message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.messageEditText.setText("")
            val date = Date()
            val formatter = SimpleDateFormat("hh:mm a")
            val dateAndTime: String = formatter.format(date)
            val msgPojo = MessageModel(message, SenderUid,dateAndTime)
            firebaseDatabase.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .push()
                .setValue(msgPojo).addOnCompleteListener {
                    firebaseDatabase.getReference().child("chats")
                        .child(reciverRoom)
                        .child("messages")
                        .push().setValue(msgPojo).addOnCompleteListener {

                        }
                }
        }
    }
}