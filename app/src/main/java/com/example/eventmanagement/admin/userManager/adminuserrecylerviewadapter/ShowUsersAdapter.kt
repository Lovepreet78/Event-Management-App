package com.example.eventmanagement.admin.userManager.adminuserrecylerviewadapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagement.R
import com.example.eventmanagement.admin.userManager.AdminUserDetail
import com.example.eventmanagement.admin.userManager.AdminUsersModel.Content

class ShowUsersAdapter(private val user:List<Content>, private val context: Context): RecyclerView.Adapter<ShowUsersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShowUsersAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_users_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = user[position]
        holder.userId.text = currentItem.username
        val role = if(currentItem.roles.contains("ADMIN") ) "ADMIN" else if(currentItem.roles.contains("MANAGEMENT")) "MANAGEMENT" else "USER"
        holder.userRole.text = role


        holder.itemView.setOnClickListener {
            val intentToUserDetail = Intent(context,AdminUserDetail::class.java)
            intentToUserDetail.putExtra("userId",currentItem.id)
            intentToUserDetail.putExtra("userName",currentItem.username)
            intentToUserDetail.putExtra("userRole",role)
            context.startActivity(intentToUserDetail)



        }


    }

    override fun getItemCount(): Int {
        return user.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userId: TextView = itemView.findViewById(R.id.adminUserName)
        val userRole: TextView = itemView.findViewById(R.id.adminUserRole)

    }

}