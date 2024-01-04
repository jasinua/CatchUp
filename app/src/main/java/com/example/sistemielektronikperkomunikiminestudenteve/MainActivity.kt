package com.example.sistemielektronikperkomunikiminestudenteve

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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
    private lateinit var auth: FirebaseAuth

    val fileName = "login"
    var sharedPref : SharedPreferences? = null
    val Username = "username"
    val Password = "password"

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context : Context = this
        mainactivity = this
        auth = Firebase.auth

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

                                    FirebaseDatabase.getInstance().getReference("USERS").child(sharedPref!!.getString(Username,"").toString()).addListenerForSingleValueEvent(object:ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            Log.d(snapshot.child("EMAIL").getValue().toString(),"account shit email")
                                            Log.d(snapshot.child("PASS").getValue().toString(),"account shit password")
                                            auth.signInWithEmailAndPassword(snapshot.child("EMAIL").getValue().toString(), snapshot.child("PASS").getValue().toString())
                                                .addOnCompleteListener(mainactivity) { task ->
                                                    if (task.isSuccessful) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        Log.d("TAG", "signInWithEmail:success")
                                                        val user = auth.currentUser
                                                        updateUI(user)
                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                                                        Toast.makeText(
                                                            baseContext,
                                                            "Authentication failed.",
                                                            Toast.LENGTH_SHORT,
                                                        ).show()
                                                        updateUI(null)
                                                    }
                                                }
                                            auth.signOut()
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }

                                    })


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
            Log.d(loginID.text.toString(),"Login id")

            loginPass = findViewById<EditText>(R.id.identity)
            loginPass.setText(sharedPref!!.getString(Password,""))
            Log.d(loginPass.text.toString(),"loginpass:")

            startNavBar(context)
        }


    }
    fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "Authentication successful", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Authentication unsuccessful", Toast.LENGTH_LONG).show()
        }
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