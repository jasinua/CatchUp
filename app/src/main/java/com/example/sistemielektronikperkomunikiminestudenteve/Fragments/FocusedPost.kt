package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Adapters.CommentsAdapter
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetCommentsModel
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
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



    private lateinit var recyclerView: RecyclerView
    private lateinit var postList:ArrayList<GetCommentsModel>
    private lateinit var mAdapter : CommentsAdapter
    lateinit var idInfo : String


    private lateinit var userId : String

    lateinit var getCommentText:EditText


    private lateinit var databaseReference: DatabaseReference

    lateinit var mainactivity : MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity
        userId = mainactivity.getUserId()


        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.setHasFixedSize(true)
        postList = arrayListOf<GetCommentsModel>()

        mainactivity = activity as MainActivity
        idInfo = mainactivity.getUserId()

        mAdapter = CommentsAdapter(postList,idInfo,context,mainactivity)
        recyclerView.adapter = mAdapter

        loadComments()


        databaseReference = FirebaseDatabase.getInstance().getReference("POSTS")


        val postCommentButton = view.findViewById<Button>(R.id.postCommentButton)

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

        getCommentText =  view.findViewById(R.id.commentText)

        postCommentButton.setOnClickListener {

            postComment()
            

        }



        //setting data to show on the focus post
        postName.text = poster
        postTitle.text = title
        postContext.text = desc
        postTime.text = posttime
        commentCount.text = comments
        likeCount.text = likes



        //get profile picture
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

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.child("likedUsers").children) {
                    if (snap.key.toString().equals("$userId")) {
                        likeButton.setImageResource(R.drawable.thumbsup)
                        return
                    }
                }
                likeButton.setImageResource(R.drawable.blankthumbsup)
            }
            override fun onCancelled(error: DatabaseError){
            }
        })

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

        //goes back to HomePageFragment
        backButton.setOnClickListener(){
            mainactivity.setCurrentFragment(HomePageFragment(position,true))
        }

    }

    private fun loadComments() {
        databaseReference =
            FirebaseDatabase.getInstance().getReference("POSTS").child(postID.toString()).child("commentSection")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    postList.clear()

                    for (snap2 in snapshot.children) {//for loopi per user posts

                        val username = snap2.child("commentuserName").getValue().toString()
                        val commentLikes = snap2.child("commentLike").getValue().toString()
                        val comments = snap2.child("commentDescription").getValue().toString()
                        val profileURL = snap2.child("commenterProfileURL").getValue().toString()


                        if (!postList.contains(
                                GetCommentsModel(comments, commentLikes, profileURL, username,username)
                            )
                        )  {
                            postList.add(

                                GetCommentsModel(comments, commentLikes, profileURL, username,username)
                            )}

                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun postComment() {

            var getCommentText = getCommentText.text.toString()

            var getUserID =  userId
            var userName = ""
            var profileURL =""


        val postID = postID


        //poster name
        FirebaseDatabase.getInstance().getReference("USERS").child("$getUserID").addListenerForSingleValueEvent(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                var commentsLike = "0"

                userName = snapshot.child("EMRI").getValue().toString()
                profileURL = snapshot.child("PROFILE").getValue().toString()
                //post id
                val postID2 = databaseReference.push().key!!


                val post = GetCommentsModel(getCommentText, commentsLike,profileURL,getUserID, userName)

                databaseReference.child(postID.toString()).child("commentSection").child(postID2).setValue(post).addOnCompleteListener{

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