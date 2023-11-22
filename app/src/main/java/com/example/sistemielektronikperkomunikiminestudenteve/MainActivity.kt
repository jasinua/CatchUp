package com.example.sistemielektronikperkomunikiminestudenteve

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.sistemielektronikperkomunikiminestudenteve.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue


class MainActivity : AppCompatActivity() {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_log_in)


        val database = Firebase.database
        val ref = database.getReference("USERS")


        val login = findViewById<Button>(R.id.logInButton)
        val loginEmail = findViewById<EditText>(R.id.idInput)
        val loginPass = findViewById<EditText>(R.id.passwordInput)

        val emailPattern = ".*\\..*@student.uni-pr.edu"

        login.setOnClickListener(){
        var exists = false
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val count = dataSnapshot.childrenCount
                    if (loginEmail.text.toString().equals("")) {
                        Toast.makeText(
                            applicationContext,
                            "Please enter a student email",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (loginPass.text.toString().equals("")) {
                        Toast.makeText(
                            applicationContext,
                            "Please enter a password",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        for (dataSnapshot1: DataSnapshot in dataSnapshot.getChildren()) {

                            var email = dataSnapshot1.child("EMAIL").getValue().toString()
                            var pass = dataSnapshot1.child("PASS").getValue().toString()


                            if (!exists) {

                                if (email.equals(loginEmail.text.toString()) && pass.equals(
                                        loginPass.text.toString()
                                    )
                                ) {
                                    exists = true
                                    Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT)
                                        .show()
                                    setContentView(R.layout.fragment_home_page)
                                }
                            }
                        }
                        if (!exists && loginEmail.text.toString().matches(Regex(emailPattern))) {

                            ref.child(count.toString()).child("EMAIL")
                                .setValue(loginEmail.text.toString())
                            ref.child(count.toString()).child("PASS")
                                .setValue(loginPass.text.toString())

                            Toast.makeText(applicationContext, "Register successful", Toast.LENGTH_SHORT)
                                .show()

                        } else if(!loginEmail.text.toString().matches(Regex(emailPattern))){
                            Toast.makeText(applicationContext, "Invalid student email", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}