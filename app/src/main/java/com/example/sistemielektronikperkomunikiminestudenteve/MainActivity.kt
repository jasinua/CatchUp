package com.example.sistemielektronikperkomunikiminestudenteve

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.DocumentsFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.HomePageFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.ProfileFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.notificationsPage
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.postFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
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

    val fileName = "login"
    var sharedPref : SharedPreferences? = null
    val Username = "username"
    val Password = "password"
    val lastKey = "key"

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context : Context = this
        mainactivity = this

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

    private fun startNavBar(context:Context){
        setContentView(R.layout.activity_main)

        createNotificationChannel()


        var builder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher_foreground))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

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

        var count = 0
    FirebaseDatabase.getInstance().getReference("USERS")
        .child(sharedPref!!.getString(Username, "").toString())
        .child("NOTIFICATIONS").orderByChild("notificationTime")
        .addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                notificationsPage()
                //qtu me ba notificatio
                if(snapshot.child("notificationSent").getValue()==false) {

                    val notificationType = snapshot.child("notificationType").getValue().toString()
                    val sender = snapshot.child("notificationSenderName").getValue()

                    if (notificationType.equals("like")) {
                        builder.setContentTitle("New like!")
                        builder.setContentText("$sender liked your post")
                    } else {
                        builder.setContentTitle("New comment!")
                        builder.setContentText("$sender commented on your post")
                    }


                    with(NotificationManagerCompat.from(context)) {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }
                        notify(1, builder.build())

                        FirebaseDatabase.getInstance().getReference("USERS")
                            .child(sharedPref!!.getString(Username, "").toString())
                            .child("NOTIFICATIONS").child(snapshot.key.toString()).child("notificationSent").setValue(true)
                    }
                }else{

                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

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