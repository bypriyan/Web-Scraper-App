package com.bypriyan.friendshub

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.friendshub.databinding.ActivityAdminBinding
import com.bypriyan.friendshub.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    var userList = mutableListOf<ModelUsers>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        reference.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists() && snapshot.hasChildren()) {
                    snapshot.children.forEach { dataSnapshot ->

                        val user = dataSnapshot.getValue(ModelUsers::class.java)

                        val email = dataSnapshot.child("email").value as String
                        val password = dataSnapshot.child("password").value as String
                        val timestamp = dataSnapshot.child("timestamp").value as String
                        val uid = dataSnapshot.child("uid").value as String

                        Log.d("AdminActivity", "User fetched: $user")
                        user?.let { userList.add(it) }
                    }
                    binding.usersRV.adapter = AdapterUsers(this@AdminActivity, userList)
                } else {
                    Toast.makeText(this@AdminActivity, "No data found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        binding.backBtn.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        //back pressed
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })


    }

}