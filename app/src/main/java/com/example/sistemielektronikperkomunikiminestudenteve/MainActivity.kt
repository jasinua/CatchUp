package com.example.sistemielektronikperkomunikiminestudenteve

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.DocumentsFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.HomePageFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.NotificationFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.ProfileFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.postFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {

    lateinit var navigation: BottomNavigationView
    lateinit var loginID: EditText
    val database = Firebase.database
    val ref = database.getReference("USERS")

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_log_in)

        val login = findViewById<Button>(R.id.logInButton)
        loginID = findViewById<EditText>(R.id.idInput)
        val loginPass = findViewById<EditText>(R.id.identity)
//        val showPass = findViewById<ImageView>(R.id.showPass)
        val idLength = 12

        var switchPass = 1

        var loggedIn = false

//        showPass.setOnClickListener(){
//            if(switchPass==1) {
//
//                loginPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                switchPass = 0
//
//            }else{
//
//                loginPass.setTransformationMethod(PasswordTransformationMethod.getInstance())
//                switchPass = 1
//            }
//        }

        if (!loggedIn) {
            login.setOnClickListener {

                ref.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {


                        if (loginID.text.toString().equals("")) {

                            Toast.makeText(
                                applicationContext,
                                "Please enter a student ID",
                                Toast.LENGTH_SHORT
                            ).show()


                        } else if (loginPass.text.toString().equals("")) {

                            Toast.makeText(
                                applicationContext,
                                "Please enter a password",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            var count = 0
                            for (user: DataSnapshot in dataSnapshot.children) {
                                count++
                                val id = user.key.toString()
                                val pass = user.child("PASS").value.toString()

                                if (id.equals(loginID.text.toString()) && pass.equals(loginPass.text.toString())) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Login successful",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    startNavBar()
                                    break
                                }else if(!id.equals(loginID.text.toString()) && !pass.equals(loginPass.text.toString()) && count==dataSnapshot.childrenCount.toInt()){
                                    Toast.makeText(
                                        applicationContext,
                                        "User doesn't exsist",
                                        Toast.LENGTH_SHORT
                                    ).show()
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

    private fun startNavBar(){
        setContentView(R.layout.activity_main)
        navigation = findViewById(R.id.bottomNavigationView)
        setCurrentFragment(HomePageFragment())


        navigation.setOnNavigationItemSelectedListener() {


            val homePage = HomePageFragment()
            val notificationPage = NotificationFragment()
            val profilePage= ProfileFragment()
            val documentPage= DocumentsFragment()
            val postPage = postFragment()


            when (it.itemId) {
                R.id.home -> setCurrentFragment(homePage)
                R.id.notification -> setCurrentFragment(notificationPage)
                R.id.profile -> setCurrentFragment(profilePage)
                R.id.documnets -> setCurrentFragment(documentPage)
                R.id.post -> setCurrentFragment(postPage)

            }
            true

        }
    }

    fun getUserId(): String {
        return loginID.text.toString()
    }
}