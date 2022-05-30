package com.applligent.admitly.chatSystem.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.applligent.admitly.R
import com.applligent.admitly.chatSystem.ChatScreenActivity
import com.applligent.admitly.chatSystem.model.Users
import com.applligent.admitly.utils.preferences.getUserId
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatListAdapter(var activity: FragmentActivity?, private var userList: List<Users>): RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var user_profileImage: CircleImageView = view.findViewById(R.id.chatList_profliePicture)
        var userName: TextView = view.findViewById(R.id.chatList_profileName)
        var last_msg: TextView = view.findViewById(R.id.last_msg)
        var messageTime: TextView = view.findViewById(R.id.messageTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item_design,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = userList[position]

        if (activity?.getUserId().toString() == users.userId) {
            holder.itemView.visibility = View.GONE
            val params = holder.itemView.layoutParams
            params.height = 0
            params.width = 0
            holder.itemView.layoutParams = params
        }

        holder.userName.text = users.name
        Picasso.get().load(users.imageUri).into(holder.user_profileImage);
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, ChatScreenActivity::class.java)
            intent.putExtra("reciver_name",users.name)
            intent.putExtra("userId",users.userId)
            activity?.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}