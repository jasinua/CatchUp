package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.compose.runtime.snapshots.Snapshot
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class postFragment : Fragment(R.layout.fragment_post) {

    private lateinit var title:EditText
    private lateinit var description:EditText

    private lateinit var databaseReference:DatabaseReference
    var max:Long = 0;



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        title = view.findViewById(R.id.title)
        description = view.findViewById(R.id.postDescription)

        databaseReference = FirebaseDatabase.getInstance().getReference("POSTS")

        view.findViewById<Button>(R.id.postButton).setOnClickListener {
            if(title.text.toString().equals("")){
                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
            }else if(description.text.toString().equals("")){
                Toast.makeText(context, "Please give a description", Toast.LENGTH_SHORT).show()
            }else {
                savePostData()
            }
        }

    }
    @SuppressLint("SuspiciousIndentation")
    private fun savePostData() {

        val title = title.text.toString()
        val description = description.text.toString()
        val postID = databaseReference.push().key!!
        val timeFormat = SimpleDateFormat("dd/M hh:mm:ss")
        val time = timeFormat.format(Date())

        val post = GetPostsModel(title, description, "0" , "0",System.currentTimeMillis(),time)

            databaseReference.child(postID).setValue(post).addOnCompleteListener{
                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }


    }

}


