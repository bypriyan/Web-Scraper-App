package com.bypriyan.friendshub.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.bypriyan.friendshub.AdminActivity
import com.bypriyan.friendshub.DataActivity
import com.bypriyan.friendshub.databinding.ActivityLoginBinding

import com.bypriyan.friendshub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val username = binding.username
        val password = binding.password
        val loginBtn = binding.login
        val loading = binding.loading
        val signIn = binding.signIn

        binding.admin?.setOnClickListener{
            startActivity(Intent(this, AdminActivity::class.java))
        }

        loginBtn.setOnClickListener {
            val email = username.text.toString()
            val pass = password.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                loading.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        loading.visibility = View.GONE
                        loginBtn.visibility = View.VISIBLE

                        if (task.isSuccessful) {
                            // Sign in success

                            val hasmap: HashMap<String, String> = HashMap()
                            hasmap["email"] = email
                            hasmap["password"] = pass
                            hasmap["uid"] = auth.currentUser!!.uid
                            hasmap["timestamp"] = (System.currentTimeMillis() - 2).toString()

                            val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
                            reference.child(auth.currentUser!!.uid).setValue(hasmap).addOnSuccessListener {
                                startActivity(Intent(this, DataActivity::class.java))
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        signIn?.setOnClickListener {
            val email = username.text.toString()
            val pass = password.text.toString()
            loading.visibility = View.VISIBLE
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener {
                    loading.visibility = View.GONE
                    startActivity(Intent(this, DataActivity::class.java))
                }.addOnFailureListener{
                    loading.visibility = View.GONE
                    Toast.makeText(this, "Authentication failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }

            }
        }

        }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, DataActivity::class.java))
    }

    }
}