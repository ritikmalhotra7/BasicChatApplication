package com.example.basicchatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basicchatapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var userList:ArrayList<User>
    private lateinit var adapter : ApplicationAdapter

    private lateinit var firebase : Firebase
    private lateinit var mAuth :FirebaseAuth
    private lateinit var DBReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        DBReference = FirebaseDatabase.getInstance("https://basic-chat-application-4d671-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        userList = ArrayList()
        adapter = ApplicationAdapter(this,userList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        DBReference.child("user").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Toast.makeText(this@MainActivity, snapshot.childrenCount.toString(),Toast.LENGTH_SHORT).show()
                userList.clear()
                for(snap in snapshot.children){
                    val currentUser = snap.getValue(User::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.side_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                mAuth.signOut()
                val intent = Intent(this@MainActivity, Login::class.java)
                finish()
                startActivity(intent)

            return true
            }
        }
        return true
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}