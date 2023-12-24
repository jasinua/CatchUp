package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.FocusedPost
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class PostsAdapter(
    private val idList: ArrayList<GetPostsModel>,
    idInfo: String,
    myContext: Context?,
    mainactivity: MainActivity
):
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_posted, parent, false)

        return ViewHolder(itemView)
    }

    var userId = idInfo
    var thisContext = myContext
    var mainactivity = mainactivity



    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val currentID = idList[position]
        holder.postName.text = currentID.poster
        holder.postTitle.text = currentID.title
        holder.postText.text = currentID.desc
        holder.postLikes.text = currentID.likes
        holder.postComments.text = currentID.comments
        holder.postTime.text = currentID.posttime
        Picasso.with(thisContext).load(currentID.profileURL).into(holder.postProfile)

        holder.postText.setOnClickListener(){
            mainactivity.setCurrentFragment(FocusedPost(position,currentID.publicKey,currentID.poster,currentID.title,currentID.desc,currentID.likes,currentID.comments,currentID.posttime,currentID.profileURL))
        }

        holder.postTitle.setOnClickListener(){
            mainactivity.setCurrentFragment(FocusedPost(position,currentID.publicKey,currentID.poster,currentID.title,currentID.desc,currentID.likes,currentID.comments,currentID.posttime,currentID.profileURL))
        }

        holder.postComments.setOnClickListener(){
            mainactivity.setCurrentFragment(FocusedPost(position,currentID.publicKey,currentID.poster,currentID.title,currentID.desc,currentID.likes,currentID.comments,currentID.posttime,currentID.profileURL))
        }

        FirebaseDatabase.getInstance().getReference("USERS").child("$userId").child("PROFILE").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Picasso.with(thisContext).load(snapshot.getValue().toString()).into(holder.commentLogo)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        val postId = currentID.publicKey
        val dbRef = FirebaseDatabase.getInstance().getReference("POSTS").child(postId.toString())

        holder.likeButton.setOnClickListener() {

                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {

                            val currentLikes = snapshot.child("likes").getValue().toString().toInt()

                                for(snap in snapshot.child("likedUsers").children) {
                                    if(snap.key.toString().equals("$userId")) {

                                        dbRef.child("likes").setValue("" + (currentLikes - 1))
                                        dbRef.child("likedUsers").child("$userId").removeValue()

                                        holder.likeButton.setImageResource(R.drawable.thumbsup2)

                                        return
                                    }
                                }


                                dbRef.child("likes").setValue("" + (currentLikes + 1))
                                dbRef.child("likedUsers").child("$userId").setValue("")
                                holder.likeButton.setImageResource(R.drawable.thumbsup)




                        }
                        override fun onCancelled(error: DatabaseError){
                        }
                    })
            }

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {


                for(snap in snapshot.child("likedUsers").children) {
                    if(snap.key.toString().equals("$userId")) {

                        holder.likeButton.setImageResource(R.drawable.thumbsup2)

                        return
                    }


                    holder.likeButton.setImageResource(R.drawable.thumbsup2)

                }





            }
            override fun onCancelled(error: DatabaseError){
            }
        })

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
        val postProfile: ImageView = itemView.findViewById(R.id.postProfile)
        val commentLogo : ImageView = itemView.findViewById(R.id.loggedInCommentLogo)


        fun showToast(message: String) {
            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
        }

    }

}


