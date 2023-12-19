package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.example.sistemielektronikperkomunikiminestudenteve.databinding.FragmentLogInBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.util.zip.Inflater


class postFragment : Fragment(R.layout.fragment_post) {

    private lateinit var userId : String
    private lateinit var databaseReference: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var DESC = view.findViewById<EditText>(R.id.PostContext)
        var TITLE = view.findViewById<EditText>(R.id.PostTitle)
        val postButton = view.findViewById<Button>(R.id.postButton)

        databaseReference = FirebaseDatabase.getInstance().getReference("USERS")



        postButton.setOnClickListener() {

            databaseReference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        for(snap in snapshot.children){
//                            if((snap.key.toString()).equals(user.text.toString())){
//                                val userId = user.text.toString()

                                userId = snap.key.toString()
                                val latestKey = findLatestPost() + 1
                                databaseReference.child(userId).child("POSTS").child("$latestKey").child("LIKES").setValue("0")
                                databaseReference.child(userId).child("POSTS").child("$latestKey").child("COMMENTS").setValue("0")
                                databaseReference.child(userId).child("POSTS").child("$latestKey").child("DESC").setValue(DESC.text.toString())
                                databaseReference.child(userId).child("POSTS").child("$latestKey").child("TITLE").setValue(TITLE.text.toString())
//                            }
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    private fun findLatestPost():Int{
        var lastKey = 0
        databaseReference.child(userId).child("POSTS").limitToLast(1).addValueEventListener(object: ValueEventListener{


            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snap in dataSnapshot.children) {
                        lastKey  = snap.key.toString().toInt()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }

            })
        return lastKey
    }

}