package com.example.sistemielektronikperkomunikiminestudenteve

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.notificationsPage
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val channelId = "i.apps.notifications"

class ServiceRunner: Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    lateinit var context : Context
    lateinit var mainactivity : MainActivity

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.

            try{
                Thread.sleep(1000)
            }catch(e:Exception){
                Thread.interrupted()
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
        }
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.

        HandlerThread("ServiceStartArguments").apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job

        val sharedPrefUser = getSharedPreferences("login",Context.MODE_PRIVATE).getString("username","").toString()
        context = applicationContext



        var notificationID = 0
        FirebaseDatabase.getInstance().getReference("USERS")
            .child(sharedPrefUser)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    notificationID = snapshot.child("NOTIFICATIONS").childrenCount.toInt()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        var builder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.roundlogo)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.roundlogo))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        FirebaseDatabase.getInstance().getReference("USERS")
            .child(sharedPrefUser)
            .child("NOTIFICATIONS").orderByChild("notificationTime")
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    notificationsPage()
                    //qtu me ba notification
                    if(snapshot.child("notificationSent").getValue()==false) {

                        val notificationType =
                            snapshot.child("notificationType").getValue().toString()
                        val sender = snapshot.child("notificationSenderName").getValue()

                        if (notificationType.equals("like")) {
                            builder.setContentTitle("New like!")
                            builder.setContentText("$sender liked your post")
                        } else {
                            builder.setContentTitle("New comment!")
                            val notificationText =
                                snapshot.child("notificationText").getValue().toString()
                            builder.setStyle(
                                NotificationCompat.BigTextStyle()
                                    .bigText("$sender: $notificationText")
                            )
                        }

                        with(NotificationManagerCompat.from(context)) {
                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                            notify(notificationID, builder.build())

                            FirebaseDatabase.getInstance().getReference("USERS")
                                .child(sharedPrefUser)
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

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
    }

}