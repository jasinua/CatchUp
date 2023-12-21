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
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.HomePageFragment
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values
import kotlinx.coroutines.NonDisposableHandle.parent

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
//        holder.likeButton. = currentID
//        holder.postTime.text = currentID.posttime


        val postId = currentID.publicKey


        holder.postComments.setOnClickListener() {

            holder.showToast(postId.toString())
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("POSTS")



        holder.likeButton.setOnClickListener() {


            holder.showToast(postId.toString())


            if (!liked) {
                dbRef.child(postId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val currentLikes = snapshot.child("likes").value.toString().toInt()
                        dbRef.child(postId.toString()).child("likes").setValue(""+(currentLikes + 1))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
                liked = true
            } else {
                dbRef.child(postId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val currentLikes = snapshot.child("likes").value.toString().toInt()
                        dbRef.child(postId.toString()).child("likes").setValue(""+(currentLikes - 1))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
                liked = false
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
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val postComments: TextView = itemView.findViewById(R.id.commentCount)

        fun showToast(message: String) {
            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
        }
//        val postTime: TextView = itemView.findViewById(R.id.)
    }

}