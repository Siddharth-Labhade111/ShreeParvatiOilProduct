package com.example.shreeparvatioilandproducts

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.shreeparvatioilandproducts.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        auth = Firebase.auth
        database = Firebase.database.reference

        // Set up button listeners
        binding.loginButton.setOnClickListener {
            val email = binding.emaillogin.text.toString().trim()
            val password = binding.passwordlogin.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all Details", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }

            binding.donthaveaccount.setOnClickListener {
                val intent=Intent(this,SignActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUi(user)
            } else {
                Toast.makeText(this, "Account does not exist. Please sign up.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SignActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Login failed. Try again.", Toast.LENGTH_SHORT).show()
        }
    }
}