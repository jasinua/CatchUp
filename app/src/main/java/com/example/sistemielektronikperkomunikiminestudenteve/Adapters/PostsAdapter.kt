package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

class PostsAdapter (private val idList:ArrayList<GetPostsModel>, idInfo:String):
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_posted, parent, false)
        return ViewHolder(itemView)
    }

    var liked by Delegates.notNull<Boolean>()
    var userId = idInfo


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentID = idList[position]
        holder.postName.text = currentID.poster
        holder.postTitle.text = currentID.title
        holder.postText.text = currentID.desc
        holder.postLikes.text = currentID.likes
        holder.postComments.text = currentID.comments
        holder.postTime.text = currentID.posttime


        val postId = currentID.publicKey
        val dbRef = FirebaseDatabase.getInstance().getReference("POSTS").child(postId.toString())

        holder.likeButton.setOnClickListener() {
            Log.d("$userId","")
            liked=false

                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {

                            val currentLikes = snapshot.child("likes").getValue().toString().toInt()

                                for(snap in snapshot.child("likedUsers").children) {
                                    if(snap.key.toString().equals("$userId")) {

                                        dbRef.child("likes").setValue("" + (currentLikes - 1))
                                        dbRef.child("likedUsers").child("$userId").removeValue()
                                        liked = false
                                        return
                                    }
                                }

                            if(!liked) {
                                dbRef.child("likes").setValue("" + (currentLikes + 1))
                                dbRef.child("likedUsers").child("$userId").setValue("")
                                liked = true
                            }

                        }

                        override fun onCancelled(error: DatabaseError){
                        }
                    })
            }
    }
    override fun getItemCount(): Int {
        return idList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postName: TextView = itemView.findViewById(R.id.postName)
        val postTitle: TextView = itemView.findViewById(R.id.PostTitle)
        val postText: TextView = itemView.findViewById(R.id.PostContext)
        val postLikes: TextView = itemView.findViewById(R.id.likeCount)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val postComments: TextView = itemView.findViewById(R.id.commentCount)
        val postTime: TextView = itemView.findViewById(R.id.postTime)

        fun showToast(message: String) {
            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
        }

    }

}