package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Adapters.PostsAdapter
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class HomePageFragment(position:Int, backClicked:Boolean) : Fragment(R.layout.fragment_home_page) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postList:ArrayList<GetPostsModel>
    private lateinit var databaseReference: Query
    private lateinit var mAdapter : PostsAdapter
    lateinit var mainactivity : MainActivity
    lateinit var idInfo : String
    var postContext = context
    var currentposition = position
    var backClicked = backClicked

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.setHasFixedSize(true)
        postList = arrayListOf<GetPostsModel>()

        mainactivity = activity as MainActivity
        idInfo = mainactivity.getUserId()

        postContext = context
        mAdapter = PostsAdapter(postList,idInfo,postContext, mainactivity)
        recyclerView.adapter = mAdapter

        getListData()

    }

    private fun getListData() {

        databaseReference =
            FirebaseDatabase.getInstance().getReference("POSTS").orderByChild("postTimeStamp")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    postList.clear()

                    for (snap2 in snapshot.children) {//for loopi per user posts

                        val title = snap2.child("title").getValue().toString()
                        val desc = snap2.child("desc").getValue().toString()
                        val poster = snap2.child("poster").getValue().toString()
                        val likes = snap2.child("likes").getValue().toString()
                        val postID = snap2.child("publicKey").getValue().toString()
                        val comments = snap2.child("comments").getValue().toString()
                        val posttime = snap2.child("posttime").getValue().toString()
                        val profileURL = snap2.child("profileURL").getValue().toString()
                        val fileUrl = snap2.child("fileUrl").getValue().toString()


                        if (!postList.contains(
                                GetPostsModel(title,desc,poster,profileURL,idInfo,likes,comments,postID, System.currentTimeMillis(), posttime,fileUrl)
                            )
                        )  {
                            postList.add(
                                GetPostsModel(title,desc,poster,profileURL,idInfo,likes,comments,postID,System.currentTimeMillis(),posttime,fileUrl))}

                    }
                    postList.reverse()
                    mAdapter.notifyDataSetChanged()
                    if(backClicked){
                        scrollToLastPosition()
                        backClicked=false
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun scrollToLastPosition(){
        recyclerView.layoutManager?.scrollToPosition(currentposition)
    }
}
