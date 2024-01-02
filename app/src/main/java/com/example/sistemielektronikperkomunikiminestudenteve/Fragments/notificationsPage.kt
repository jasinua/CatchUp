package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Adapters.NotificationsAdapter
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetNotificationsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class notificationsPage : Fragment(R.layout.fragment_notifications_page) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postList:ArrayList<GetNotificationsModel>
    private lateinit var databaseReference: Query
    private lateinit var mAdapter : NotificationsAdapter
    lateinit var mainactivity : MainActivity
    lateinit var idInfo : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.setHasFixedSize(true)
        postList = arrayListOf<GetNotificationsModel>()

        mainactivity = activity as MainActivity
        idInfo = mainactivity.getUserId()

        mAdapter = NotificationsAdapter(postList,context)
        recyclerView.adapter = mAdapter

        getListData()

        view.findViewById<Button>(R.id.clearNotifications).setOnClickListener(){
            FirebaseDatabase.getInstance().getReference("USERS").child("$idInfo").child("NOTIFICATIONS").removeValue().addOnCompleteListener(){
                Toast.makeText(context,"Notifications have been cleared", Toast.LENGTH_SHORT).show()
                mainactivity.setCurrentFragment(notificationsPage())
            }
        }

    }

    private fun getListData() {

        databaseReference =
            FirebaseDatabase.getInstance().getReference("USERS").child("$idInfo").child("NOTIFICATIONS").orderByChild("notificationTime")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    postList.clear()

                    for (snap2 in snapshot.children) {//for loopi per user posts

                        val notificationSenderID = snap2.child("notificationSenderID").getValue().toString()
                        val notificationSenderName = snap2.child("notificationSenderName").getValue().toString()
                        val notificationOfPost = snap2.child("notificationOfPost").getValue().toString()
                        val notificationType = snap2.child("notificationType").getValue().toString()
                        val notificationTime = snap2.child("notificationTime").getValue().toString()
                        val notificationSenderProfileURL = snap2.child("notificationSenderProfileURL").getValue().toString()
                        val notificationSent = snap2.child("notificationSent").getValue().toString().toBoolean()

                        if (!postList.contains(
                                GetNotificationsModel(notificationSenderID,notificationSenderName,notificationOfPost,notificationType,notificationTime,notificationSenderProfileURL,notificationSent)
                            )
                        )  {
                            postList.add(
                                GetNotificationsModel(notificationSenderID,notificationSenderName,notificationOfPost,notificationType,notificationTime,notificationSenderProfileURL,notificationSent)
                            )}

                    }
                    postList.reverse()
                    mAdapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}