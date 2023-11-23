package com.example.sistemielektronikperkomunikiminestudenteve

import android.annotation.SuppressLint
import android.os.Bundle
import android.service.voice.VoiceInteractionSession.VisibleActivityCallback
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.sistemielektronikperkomunikiminestudenteve.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import org.w3c.dom.DocumentFragment

//je kan tu munu me e nrreq mu ba switch me navigation bar se nuk te bajke the when() - 2:44am
class MainActivity : AppCompatActivity() {

    lateinit var navigation: BottomNavigationView
    val database = Firebase.database
    val ref = database.getReference("USERS")

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_log_in)

        val login = findViewById<Button>(R.id.logInButton)
        val loginID = findViewById<EditText>(R.id.idInput)
        val loginPass = findViewById<EditText>(R.id.passwordInput)


        val idLength = 12

        val menu: Menu

        var loggedIn = false

        Log.d("TAG", "LOGGED IN O FALSE")

        if (!loggedIn) {
            login.setOnClickListener {

                ref.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {


                        if (loginID.text.toString().equals("")) {

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


                            for (dataSnapshot1: DataSnapshot in dataSnapshot.children) {

                                val id = dataSnapshot1.key.toString()
                                val pass = dataSnapshot1.child("PASS").value.toString()


                                if (id.equals(loginID.text.toString()) && pass.equals(loginPass.text.toString())) {
                                    Log.d("TAG", "login works")
                                    Toast.makeText(
                                        applicationContext,
                                        "Login successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loggedIn = true
                                    setContentView(R.layout.activity_main)
                                    navigation = findViewById(R.id.bottomNavigationView)
                                    setCurrentFragment(HomePageFragment())


                                    navigation.setOnNavigationItemSelectedListener() {


                                        val homePage = HomePageFragment()
                                        val notificationPage = NotificationFragment()
//            val profilePage=ProfileFragment()
//            val documentPage=DocumentFragment()
                                        val postPage = postFragment()


                                        when (it.itemId) {
                                            R.id.home -> setCurrentFragment(homePage)
                                            R.id.notification -> setCurrentFragment(notificationPage)
//                    R.id.settings -> setCurrentFragment(profilePage)
//                    R.id.person -> setCurrentFragment(documnetPage)
                                            R.id.post -> setCurrentFragment(postPage)

                                        }
                                        true

                                    }
                                }


                            }

                            if (!(loginID.text.toString().length == idLength)) {
                                Toast.makeText(
                                    applicationContext,
                                    "Invalid student ID",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }
        }


    }
        private fun setCurrentFragment(fragment:Fragment)=
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment,fragment)
                commit()
            }
}