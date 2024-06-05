package com.bypriyan.friendshub

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bypriyan.bustrackingsystem.utility.Constants
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdapterUsers(private val context: Context, private val userList: List<ModelUsers>):
    RecyclerView.Adapter<AdapterUsers.HolderUsers>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderUsers {
        val view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false)
        return HolderUsers(view)    }

    override fun onBindViewHolder(holder: HolderUsers, position: Int) {
        val modelUser = userList[position]

        holder.emailTv.text = modelUser.email
        holder.passwordTv.text = modelUser.password
        var isAllowed = false
        if(System.currentTimeMillis()>modelUser.timestamp.toLong()){
            holder.isAllowedIv.setImageResource(R.drawable.ic_allowed)
            isAllowed = true
        }else{
            holder.isAllowedIv.setImageResource(R.drawable.ic_cancel)
            isAllowed = false
        }

        holder.isAllowedIv.setOnClickListener {
            //access do
            if(isAllowed){
                val hasmap: HashMap<String, Any> = HashMap()
                hasmap["email"] = modelUser.email
                hasmap["password"] = modelUser.password
                hasmap["uid"] = modelUser.uid
                hasmap["timestamp"] = (System.currentTimeMillis() + 86400000).toString()

                val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
                reference.child(modelUser.uid).updateChildren(hasmap).addOnSuccessListener {
                    holder.isAllowedIv.setImageResource(R.drawable.ic_cancel)
                    isAllowed = false
                }

            }else{
                var hasmap:HashMap<String, Any> = HashMap()
                hasmap.put("email", modelUser.email)
                hasmap.put("password", modelUser.password)
                hasmap.put("uid", modelUser.uid)
                hasmap.put("timestamp", (System.currentTimeMillis()-1).toString())
                var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
                reference.child(modelUser.uid).updateChildren(hasmap).addOnSuccessListener {
                    holder.isAllowedIv.setImageResource(R.drawable.ic_allowed)
                    isAllowed = true
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class HolderUsers(itemView:View): RecyclerView.ViewHolder(itemView) {
        val isAllowedIv: ImageView = itemView.findViewById(R.id.isAllowedIv)
        val emailTv: TextView = itemView.findViewById(R.id.emailTv)
        val passwordTv: TextView = itemView.findViewById(R.id.passwordTv)
    }
}