package com.example.loginsignupswm

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private var users: List<User>, context: Context):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inbox_fragment, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        holder.tvchatuser.text = user.fullname
        holder.tvchatemail.text = user.email
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvchatuser: TextView = itemView.findViewById(R.id.tv_chatuser)
        val tvchatemail: TextView = itemView.findViewById(R.id.tv_chatemail)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newUsers: List<User>){
        users = newUsers
        notifyDataSetChanged()
    }
}