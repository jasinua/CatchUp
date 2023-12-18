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
    private lateinit var idList:ArrayList<GetPostsModel>
    private lateinit var databaseReference: DatabaseReference

    private val binding by viewBinding(FragmentLogInBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        idList = arrayListOf<GetPostsModel>()

        getListData()


    }

    private fun getListData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("USERS")

        databaseReference.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                idList.clear()

                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val title = snap.child("POSTS").child("0").child("TITLE").getValue().toString()
                        val desc = snap.child("POSTS").child("0").child("DESC").getValue().toString()
                        val likes = snap.child("POSTS").child("0").child("LIKES").getValue().toString()
                        val comments = snap.child("POSTS").child("0").child("COMMENTS").getValue().toString()
                        idList.add(GetPostsModel(title,desc,likes,comments))
                    }
                    val mAdapter = PostsAdapter(idList)
                    recyclerView.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}
