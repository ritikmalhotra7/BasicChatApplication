package com.example.basicchatapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicchatapplication.databinding.ActivityChatBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ActivityChat : Activity() {
    private var _binding : ActivityChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter : MessageAdapter
    private lateinit var messageList : ArrayList<Message>

    var senderRoom : String? = null
    var recieverRoom : String?= null
    var lastPosition : Int? = null

    val lastp = "last"



  /*  var last : Int? = null*/

    private lateinit var dbRefrence : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*SharedPreferences getPrefs= PreferenceManager.getDefaultSharedPreferences (getBaseContext());
        lastPosition = getPrefs.getInt( s: "lastPos", : 0);
        recyclerView. scrollToPosition (lastPosition);*/




        dbRefrence =  FirebaseDatabase
            .getInstance("https://basic-chat-application-4d671-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference()

        val name = intent.getStringExtra("name")
        val recieverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid!!

        senderRoom = recieverUid + senderUid
        recieverRoom = senderUid + recieverUid

        actionBar?.title = name

        val ll = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = ll

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

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        binding.send.setOnClickListener {
            val message = binding.messageBox.text.toString()
            Log.d("taget",message)
            val messageObject = Message(message,senderUid)

            if(message.isNotEmpty()) {
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

   /* override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    /*override fun onDestroy() {
        super.onDestroy()
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(lastp, lastPosition!!)
            apply()
        }*//*SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = getPrefs.edit();
        e.putInt("lastPos", lastPosition);
        e.apply();*//*
    }*/
}