package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class postFragment : Fragment(R.layout.fragment_post) {

    private lateinit var title:EditText
    private lateinit var description:EditText
    private lateinit var idInfo : String

    private lateinit var databaseReference:DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        idInfo = activity.getUserId()

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
    private fun savePostData() {

        val title = title.text.toString()
        val description = description.text.toString()
        var poster = ""
        var profileURL = ""



        //poster name
        FirebaseDatabase.getInstance().getReference("USERS").child("$idInfo").addListenerForSingleValueEvent(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                poster = snapshot.child("EMRI").getValue().toString()
                profileURL = snapshot.child("PROFILE").getValue().toString()
                //post id
                val postID = databaseReference.push().key!!

                //post time
                val timeFormat = SimpleDateFormat("dd/M hh:mm:ss")
                val time = timeFormat.format(Date())

                val post = GetPostsModel(title, description, poster, profileURL, idInfo, "0" , "0",postID,System.currentTimeMillis(),time)

                databaseReference.child(postID).setValue(post).addOnCompleteListener{
                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }



            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

}


