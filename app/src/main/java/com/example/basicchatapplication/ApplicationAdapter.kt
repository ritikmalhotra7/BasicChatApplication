package com.example.basicchatapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basicchatapplication.databinding.ActivityLoginBinding.inflate
import com.example.basicchatapplication.databinding.UserLayoutBinding

class ApplicationAdapter (val context : Context, val userList : ArrayList<User>) :
        RecyclerView.Adapter<ApplicationAdapter.ViewHolder>() {

    class ViewHolder(private val binding: UserLayoutBinding/*view:View*/) : RecyclerView.ViewHolder(binding.root) {
        val textName = binding.name
        /*val email = binding.emailid*/
        val v = binding.root
        /*val textName = view.findViewById<TextView>(R.id.name)
        val emailid = view.findViewById<TextView>(R.id.emailid)*/

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
      /*  val view = LayoutInflater.from(parent.context).inflate(R.layout.user_layout,parent,false)
        return ViewHolder(view)*/
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*val currentUser = userList[position]
        holder.textName.text = currentUser.name
        holder.email.text = currentUser.email*/
        val currentUser = userList[position]
        holder.textName.text = currentUser.name
        /*holder.email.text = currentUser.email*/

        holder.v.setOnClickListener {
            val intent = Intent(context,ActivityChat::class.java)

            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}