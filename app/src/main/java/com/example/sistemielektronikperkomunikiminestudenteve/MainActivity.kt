package com.example.sistemielektronikperkomunikiminestudenteve

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_log_in)


        val database = Firebase.database
        val ref = database.getReference("USERS")


        val login = findViewById<Button>(R.id.logInButton)
        val loginEmail = findViewById<EditText>(R.id.idInput)
        val loginPass = findViewById<EditText>(R.id.passwordInput)

        login.setOnClickListener(){

        var exists = false
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val count = dataSnapshot.childrenCount

                    for(dataSnapshot1 : DataSnapshot in dataSnapshot.getChildren()){

                        var email = dataSnapshot1.child("EMAIL").getValue().toString()
                        var pass = dataSnapshot1.child("PASS").getValue().toString()

                        if(!exists) {

                            if (email.equals(loginEmail.text.toString()) && pass.equals(loginPass.text.toString())) {
                                exists = true
                                setContentView(R.layout.fragment_home_page)
                            }
                        }


                    }

                    for(dataSnapshot1 : DataSnapshot in dataSnapshot.getChildren()){

                        var email = dataSnapshot1.child("EMAIL").getValue().toString()
                        var pass = dataSnapshot1.child("PASS").getValue().toString()

                        if(!exists){
                                ref.child(count.toString()).child("EMAIL")
                                    .setValue(loginEmail.text.toString())
                                ref.child(count.toString()).child("PASS")
                                    .setValue(loginPass.text.toString())

                        }

                    }



                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        }


    }

}