package com.example.basicchatapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basicchatapplication.databinding.ActivityChatBinding
import com.example.basicchatapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ActivityChat : AppCompatActivity() {
    private var _binding : ActivityChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter : MessageAdapter
    private lateinit var messageList : ArrayList<Message>

    var senderRoom : String? = null
    var recieverRoom : String?= null

  /*  var last : Int? = null*/

    private lateinit var dbRefrence : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRefrence =  FirebaseDatabase
            .getInstance("https://basic-chat-application-4d671-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference()

        val name = intent.getStringExtra("name")
        val recieverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid!!

        senderRoom = recieverUid + senderUid
        recieverRoom = senderUid + recieverUid

        supportActionBar?.title = name
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        messageList = ArrayList()
        mAdapter = MessageAdapter(this,messageList)
        binding.recyclerView.adapter = mAdapter
  /*      binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                last = (binding.recyclerView.layoutManager as LinearLayoutManager).getPosition((binding.recyclerView.layoutManager as LinearLayoutManager).findViewByPosition(0)!!)
            }
        })
*/
        dbRefrence.child("chats").child(senderRoom.toString()).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for(snap in snapshot.children){
                    val message = snap.getValue(Message::class.java)
                    /*messageList[position]*/
                    messageList.add(message!!)
                }
                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.send.setOnClickListener {
            val message = binding.messageBox.text.toString()
            val messageObject = Message(message,senderUid)

            if(message.toString().length>0) {
                dbRefrence.child("chats").child(senderRoom.toString()).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        dbRefrence.child("chats").child(recieverRoom.toString()).child("messages").push()
                            .setValue(messageObject)
                    }
                binding.messageBox.setText("")
            }else{
                Toast.makeText(applicationContext, "Please enter some message! ", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
/*
    override fun onDestroy() {
        super.onDestroy()
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("last", last!!)
            apply()
        }
    }*/
}