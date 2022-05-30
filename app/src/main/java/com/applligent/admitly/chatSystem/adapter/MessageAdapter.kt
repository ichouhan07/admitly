package com.applligent.admitly.chatSystem.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.applligent.admitly.R
import android.widget.TextView
import com.applligent.admitly.chatSystem.model.MessageModel
import com.applligent.admitly.utils.preferences.getUserId
import java.util.ArrayList

class MessageAdapter(var context: Context, var messagesArrayList: ArrayList<MessageModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var ITEM_SEND = 1
    var ITEM_RECIEVE = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SEND) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.sender_layout_item, parent, false)
            SenderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(context).inflate(R.layout.reciver_layout_item, parent, false)
            ReciverViewHolder(view)
        }
    }


    override fun getItemCount(): Int {
        return messagesArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        val messagePojo = messagesArrayList[position]
        return if (context.getUserId().toString() == messagePojo.senderId) {
            ITEM_SEND
        } else {
            ITEM_RECIEVE
        }
    }

    internal inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_message: TextView
        var message_time: TextView

        init {
            txt_message = itemView.findViewById(R.id.chat_message)
            message_time = itemView.findViewById(R.id.message_time)
        }
    }

    internal inner class ReciverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_message: TextView
        var message_time: TextView

        init {
            txt_message = itemView.findViewById(R.id.chat_message)
            message_time = itemView.findViewById(R.id.message_time)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messagePojo = messagesArrayList[position]
        if (holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            viewHolder.txt_message.text = messagePojo.message
            viewHolder.message_time.text = messagePojo.timeStamp
        } else {
            val viewHolder = holder as ReciverViewHolder
            viewHolder.txt_message.text = messagePojo.message
            viewHolder.message_time.text = messagePojo.timeStamp
        }
    }
}