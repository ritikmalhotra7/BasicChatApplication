package com.example.basicchatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        messageList = ArrayList()
        mAdapter = MessageAdapter(this,messageList)
        binding.recyclerView.adapter = mAdapter

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

            dbRefrence.child("chats").child(senderRoom.toString()).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    dbRefrence.child("chats").child(recieverRoom.toString()).child("messages").push()
                        .setValue(messageObject)
                }
            binding.messageBox.setText("")
        }


    }
}