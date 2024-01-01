package com.example.sistemielektronikperkomunikiminestudenteve

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.DocumentsFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.HomePageFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.ProfileFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.notificationsPage
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.postFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    lateinit var mainactivity : MainActivity
    lateinit var navigation: BottomNavigationView
    lateinit var loginID: EditText
    lateinit var loginPass: EditText
    var idInfo : String? = null
    var loggedIn by Delegates.notNull<Boolean>()

    val database = Firebase.database
    val ref = database.getReference("USERS")

    val fileName = "login"
    var sharedPref : SharedPreferences? = null
    val Username = "username"
    val Password = "password"


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE)

        if(!sharedPref!!.contains(Username)) {

            setContentView(R.layout.fragment_log_in)

        val login = findViewById<Button>(R.id.logInButton)
        loginID = findViewById<EditText>(R.id.idInput)
        loginPass = findViewById<EditText>(R.id.identity)

            loginID.setText("")
            loginPass.setText("")

        val idLength = 12

        loggedIn = false

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

                        }else if (!(loginID.text.toString().length == idLength)) {
                            Toast.makeText(
                                applicationContext,
                                "Invalid student ID",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else {
                            var count = 0
                            for (user: DataSnapshot in dataSnapshot.children) {
                                count++
                                val id = user.key.toString()
                                val pass = user.child("PASS").value.toString()

                                if(id.equals(loginID.text.toString())&&!pass.equals(loginPass.text.toString())){
                                    Toast.makeText(
                                        applicationContext,
                                        "Password is incorrect",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    break
                                } else if (id.equals(loginID.text.toString()) && pass.equals(loginPass.text.toString())) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Login successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    idInfo = loginID.text.toString()

                                    val editor = sharedPref!!.edit()
                                    editor.putString(Username,idInfo)
                                    editor.putString(Password,pass)
                                    editor.commit()

                                    startNavBar()
                                    break
                                }else if(!id.equals(loginID.text.toString()) && !pass.equals(loginPass.text.toString()) && count==dataSnapshot.childrenCount.toInt()){
                                    Toast.makeText(
                                        applicationContext,
                                        "User doesn't exist",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }
        }


        }else{
            setContentView(R.layout.fragment_log_in)
            loginID = findViewById<EditText>(R.id.idInput)
            loginID.setText(sharedPref!!.getString(Username,""))
            idInfo = loginID.text.toString()
            Log.d(loginID.text.toString(),"Login id")

            loginPass = findViewById<EditText>(R.id.identity)
            loginPass.setText(sharedPref!!.getString(Password,""))
            Log.d(loginPass.text.toString(),"loginpass:")

            startNavBar()
        }


    }
    fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    private fun startNavBar(){
        setContentView(R.layout.activity_main)

        navigation = findViewById(R.id.bottomNavigationView)
        setCurrentFragment(HomePageFragment(0,false))

        navigation.setOnNavigationItemSelectedListener() {

            val homePage = HomePageFragment(0,false)
            val notificationPage = notificationsPage()
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

        //qtu me ba notification

        FirebaseDatabase.getInstance().getReference("USERS").child(sharedPref!!.getString(Username,"").toString()).child("NOTIFICATIONS").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //qtu o listener qe ka me ngu currently logged in user notifications ndatabaz edhe kur tndrron me ja qu notification
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        //qtu me ba notificiton
    }

    fun getUserId(): String {
        return idInfo.toString()
    }

    fun resetInfo(){
        this.recreate()
    }

    fun returnPref(): SharedPreferences{
        return sharedPref!!
    }

}