package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Adapters.PostsAdapter
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.example.sistemielektronikperkomunikiminestudenteve.databinding.FragmentLogInBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class HomePageFragment : Fragment(R.layout.fragment_home_page) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postList:ArrayList<GetPostsModel>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAdapter : PostsAdapter


    private val binding by viewBinding(FragmentLogInBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        postList = arrayListOf<GetPostsModel>()
        mAdapter = PostsAdapter(postList)
        recyclerView.adapter = mAdapter

        getListData()


    }

    private fun getListData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("USERS")

        databaseReference.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {

                    postList.clear()

                    for (snap in snapshot.children) {//for loopi per users
                        for (snap2 in snap.child("POSTS").children) {//for loopi per user posts

                                val title =
                                    snap.child("POSTS").child("" + snap2.key).child("TITLE")
                                        .getValue().toString()
                                val desc =
                                    snap.child("POSTS").child("" + snap2.key).child("DESC")
                                        .getValue().toString()
                                val likes =
                                    snap.child("POSTS").child("" + snap2.key).child("LIKES")
                                        .getValue().toString()
                                val comments =
                                    snap.child("POSTS").child("" + snap2.key).child("COMMENTS")
                                        .getValue()
                                        .toString()
                                val key = snap2.key.toString()

                            if(!postList.contains(GetPostsModel(title, desc, likes, comments,key))) {
                                postList.add(GetPostsModel(title, desc, likes, comments, key))
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}
