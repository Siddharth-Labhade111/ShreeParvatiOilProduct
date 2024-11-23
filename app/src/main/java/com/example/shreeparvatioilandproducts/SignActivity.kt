package com.example.shreeparvatioilandproducts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.shreeparvatioilandproducts.databinding.ActivitySignBinding
import com.example.shreeparvatioilandproducts.model.UserModel


class SignActivity : AppCompatActivity() {

    private lateinit var auth :FirebaseAuth
    private lateinit var email :String
    private lateinit var password:String
    private lateinit var name: String
    private lateinit var database :DatabaseReference

    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialization firebase auth
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.createuserbutton.setOnClickListener {
            name = binding.namesignup.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Enter Valid Credentials", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }
        binding.dontHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detailFoodName)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    private fun createAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(){
                task->
            if(task.isSuccessful){
                Toast.makeText(this,"Account created successfully",Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"Account Creation failed",Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount: Failure",task.exception)
            }
        }
    }


    private fun saveUserData(){
        name = binding.namesignup.text.toString()
        email= binding.email.text.toString().trim()
        password=binding.password.text.toString().trim()

        val user = UserModel(name,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        database.child("user").child(userId).setValue(user)
    }
}

