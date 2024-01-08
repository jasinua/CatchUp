package com.example.sistemielektronikperkomunikiminestudenteve

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlin.properties.Delegates


private const val channelId = "i.apps.notifications"
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

        val context : Context = this
        mainactivity = this


        sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE)

        if(!sharedPref!!.contains(Username)) {

            setContentView(R.layout.fragment_log_in)


            val signUpText = findViewById<TextView>(R.id.signUpText)

            signUpText.setOnClickListener {
                setContentView(R.layout.fragment_sign_up)

                val createAccountButton = findViewById<Button>(R.id.signUpButton)
                val studentID = findViewById<EditText>(R.id.studentID)
                val emriMbiemri = findViewById<EditText>(R.id.emriMbiemri)
                val email = findViewById<EditText>(R.id.email)
                val fakullteti = findViewById<EditText>(R.id.fakullteti)
                val passwordSignUp = findViewById<EditText>(R.id.passwordSignUp)
                val repeatPasswordSignUp = findViewById<EditText>(R.id.repeatPasswordSignUp)

                var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("USERS")

                val customEmailPattern = Regex("^\\S+@student\\.uni-pr\\.edu$")
                createAccountButton.setOnClickListener {

                    if (studentID.text.toString() == "" ) {
                        Toast.makeText(this, "ID Field is empty", Toast.LENGTH_SHORT).show()
                    }else if (studentID.text.toString().length >14 || studentID.text.toString().length < 12) {
                        Toast.makeText(this, "Invalid studentID length", Toast.LENGTH_SHORT).show()
                    } else if(emriMbiemri.text.toString() == "") {

                        Toast.makeText(this, "Please fill your name", Toast.LENGTH_SHORT).show()
                    } else if(email.text.toString() =="") {
                        Toast.makeText(this, "Please fill your email", Toast.LENGTH_SHORT).show()
                    } else if(!customEmailPattern.matches(email.text.toString())) {

                        Toast.makeText(this, "Please fill a valid student email", Toast.LENGTH_SHORT).show()
                    } else if (fakullteti.text.toString() =="") {

                        Toast.makeText(this, "Please fill your university", Toast.LENGTH_SHORT).show()
                    } else if (passwordSignUp.text.toString().length < 8){

                        Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                    } else if(!repeatPasswordSignUp.text.toString().equals(passwordSignUp.text.toString())) {

                        Toast.makeText(this, "Passwords dont match", Toast.LENGTH_SHORT).show()
                    } else {
                        var poster = ""
                        var profileURL = ""

                        //poster name
                        FirebaseDatabase.getInstance().getReference("USERS").addListenerForSingleValueEvent(object:ValueEventListener{

                            override fun onDataChange(snapshot: DataSnapshot) {



                                var boolean = false

                                for(snap in snapshot.children) {
                                    if(snap.key!!.toString().equals(studentID.text.toString())) {
                                        boolean = true

                                    }
                                }

                                if (!boolean) {

                                    databaseReference.child(studentID.text.toString()).child("DREJTIMI").setValue(fakullteti.text.toString())
                                    databaseReference.child(studentID.text.toString()).child("EMAIL").setValue(email.text.toString())
                                    databaseReference.child(studentID.text.toString()).child("EMRI").setValue(emriMbiemri.text.toString())
                                    databaseReference.child(studentID.text.toString()).child("PASS").setValue(passwordSignUp.text.toString())
                                    databaseReference.child(studentID.text.toString()).child("PROFILE").setValue("https://firebasestorage.googleapis.com/v0/b/seks-f1000.appspot.com/o/ProfilePictures%2Fdefult.jpg?alt=media&token=33cf33bd-f5d4-4e34-bf1c-af535d529011")

                                    Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
                                } else {

                                    Toast.makeText(context, "Account already exists", Toast.LENGTH_SHORT).show()
                                }


                            }
                            override fun onCancelled(error: DatabaseError) {
                            }

                        })

                    }



                }


                findViewById<TextView>(R.id.gotoLogin).setOnClickListener {
                    resetInfo()
                }


            }

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

                                        startNavBar(context)
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

            loginPass = findViewById<EditText>(R.id.identity)
            loginPass.setText(sharedPref!!.getString(Password,""))

            startNavBar(context)
        }


    }
    fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    private fun startNavBar(context:Context){
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        navigation = findViewById(R.id.bottomNavigationView)
        setCurrentFragment(HomePageFragment(0,false))

        navigation.setOnNavigationItemSelectedListener() {

            val homePage = HomePageFragment(0,false)
            val notificationPage = notificationsPage()
            val profilePage= ProfileFragment()
            val documentPage= DocumentsFragment(applicationContext)
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

        startService(Intent(this, ServiceRunner::class.java))

    }

    fun serviceStopper(){
        stopService(Intent(this, ServiceRunner::class.java))
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= 0){
            val  channel = NotificationChannel(channelId,"Notifications channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Test description for channel"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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