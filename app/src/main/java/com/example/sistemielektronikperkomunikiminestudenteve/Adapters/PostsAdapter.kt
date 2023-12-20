package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values

class PostsAdapter (private val idList:ArrayList<GetPostsModel>):
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_posted, parent, false)
        return ViewHolder(itemView)
    }

    var liked : Boolean = false

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentID = idList[position]
        holder.postTitle.text = currentID.title
        holder.postText.text = currentID.desc
        holder.postLikes.text = currentID.likes
        holder.postComments.text = currentID.comments
        holder.postTime.text = currentID.posttime

        val dbRef = FirebaseDatabase.getInstance().getReference("POSTS")

        holder.likeButton.setOnClickListener() {

            if (!liked) {

                dbRef.child("-Nm6o2_CN8eQpV6SG6dR").addListenerForSingleValueEvent(object :
                    ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        dbRef.child("-Nm6o2_CN8eQpV6SG6dR").child("likes")
                            .setValue(snapshot.child("likes").getValue().toString().toInt() + 1)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
                liked=true

            } else if(liked){

                dbRef.child("-Nm6o2_CN8eQpV6SG6dR").addListenerForSingleValueEvent(object :
                    ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        dbRef.child("-Nm6o2_CN8eQpV6SG6dR").child("likes")
                            .setValue(snapshot.child("likes").getValue().toString().toInt() - 1)

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

                liked=false
            }
        }
    }

    override fun getItemCount(): Int {
        return idList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTitle: TextView = itemView.findViewById(R.id.PostTitle)
        val postText: TextView = itemView.findViewById(R.id.PostContext)
        val postLikes: TextView = itemView.findViewById(R.id.likeCount)
        val postComments: TextView = itemView.findViewById(R.id.commentCount)
        val postTime: TextView = itemView.findViewById(R.id.postTime)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
    }
}