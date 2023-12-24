package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class FocusedPost(position:Int ,postID: String?,poster: String?,title: String?,desc: String?,likes: String?,comments: String?,posttime: String?,profileURL: String?) : Fragment(R.layout.fragment_focused_post) {

    val postID = postID
    val poster = poster
    val title = title
    val desc = desc
    val likes = likes
    val comments = comments
    val posttime = posttime
    val profileURL = profileURL
    val position = position

    lateinit var mainactivity : MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity
        val userId = mainactivity.getUserId()

        val postName = view.findViewById<TextView>(R.id.PostName)
        val postTitle = view.findViewById<TextView>(R.id.PostTitle)
        val postContext = view.findViewById<TextView>(R.id.PostContext)
        val postTime = view.findViewById<TextView>(R.id.postTime)
        val commentCount = view.findViewById<TextView>(R.id.commentCount)
        val likeCount = view.findViewById<TextView>(R.id.likeCount)
        val likeButton = view.findViewById<ImageView>(R.id.likeButton)
        val posterProfile = view.findViewById<ImageView>(R.id.posterProfile)
        val commentLogo = view.findViewById<ImageView>(R.id.commentLogo)
        val backButton = view.findViewById<ImageView>(R.id.backButton)

        postName.text = poster
        postTitle.text = title
        postContext.text = desc
        postTime.text = posttime
        commentCount.text = comments
        likeCount.text = likes

        Picasso.with(context).load(profileURL).into(posterProfile)

        FirebaseDatabase.getInstance().getReference("USERS").child("$userId").child("PROFILE").addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Picasso.with(context).load(snapshot.getValue().toString()).into(commentLogo)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


        //like thing
        val dbRef = FirebaseDatabase.getInstance().getReference("POSTS").child(postID.toString())

        likeButton.setOnClickListener() {
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentLikes = snapshot.child("likes").getValue().toString().toInt()
                    for(snap in snapshot.child("likedUsers").children) {
                        if(snap.key.toString().equals("$userId")) {
                            dbRef.child("likes").setValue("" + (currentLikes - 1))
                            likeCount.text = (currentLikes - 1).toString()
                            dbRef.child("likedUsers").child("$userId").removeValue()
                            likeButton.setImageResource(R.drawable.blankthumbsup)
                            return
                        }
                    }
                        dbRef.child("likes").setValue("" + (currentLikes + 1))
                        likeCount.text = (currentLikes + 1).toString()
                        dbRef.child("likedUsers").child("$userId").setValue("")
                        likeButton.setImageResource(R.drawable.thumbsup)
                }
                override fun onCancelled(error: DatabaseError){
                }
            })
        }

        backButton.setOnClickListener(){
            mainactivity.setCurrentFragment(HomePageFragment(position,true))
        }

    }

}