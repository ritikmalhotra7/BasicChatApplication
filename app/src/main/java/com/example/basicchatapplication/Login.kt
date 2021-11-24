package com.example.basicchatapplication

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.example.basicchatapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.signupbutton.setOnClickListener {
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }
        binding.loginbutton.setOnClickListener {
            val email = binding.emailid.text.toString()
            val password = binding.password.text.toString()

            login(email,password)
        }
        binding.loginPhone.setOnClickListener {
            startActivity(Intent(this,Phone_verification::class.java))
            finish()
        }
    }

    private fun login(email: String, password: String) {
       if(!TextUtils.isEmpty(binding.emailid.text.toString()) && !TextUtils.isEmpty(binding.password.text.toString())){
           mAuth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener(this) { task ->
                   if (task.isSuccessful) {
                       val intent = Intent(this@Login,MainActivity::class.java)
                       finish()
                       startActivity(intent)
                   } else {
                       Toast.makeText(this@Login, "Authentication failed.",
                           Toast.LENGTH_SHORT).show()
                   }
               }
       }else{
           Toast.makeText(this@Login , "Please Enter Something", Toast.LENGTH_SHORT).show()
       }
    }

    override fun onStart() {
        /*super.onStart()*/
        val mAuth = FirebaseAuth.getInstance().currentUser
        if(mAuth != null){
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            super.onStart()
        }
        //create sharedPrefrences store uid when user login /sign up ,remove uid on logout

    }
}