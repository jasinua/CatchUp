package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetCommentsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class CommentsAdapter(
    private val idList: ArrayList<GetCommentsModel>,
    userId: String,
    myContext: Context?,
    mainactivity: MainActivity,
    postID: String,
):
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    var thisContext = myContext
    var postID = postID
    var userId = userId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.commentpart, parent, false)

        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val currentID = idList[position]
        holder.commentLikes.text = currentID.commentLike
        holder.commentDescription.text = currentID.commentDescription
        holder.userName.text = currentID.commentuserName
        holder.postTime.text = currentID.commentTime
        Picasso.with(thisContext).load(currentID.commenterProfileURL).into(holder.commentLogo)


        //ref for every comment
        val dbRef = FirebaseDatabase.getInstance().getReference("POSTS").child(postID)
            .child("commentSection").child(currentID.commentID.toString())

        //i qet images per likes te comments
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.child("likedUsers").children) {
                    if (snap.key.toString().equals("$userId")) {
                        holder.likeButton.setImageResource(R.drawable.thumbsup)
                        return
                    }
                }
                holder.likeButton.setImageResource(R.drawable.blankthumbsup)
            }
            override fun onCancelled(error: DatabaseError){
            }
        })

        //likes / dislikes a comment
        holder.likeButton.setOnClickListener() {
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val currentCommentLikes = snapshot.child("commentLike").getValue().toString().toInt()

                    for(snap in snapshot.child("likedUsers").children) {
                        if(snap.key.toString().equals("$userId")) {
                            dbRef.child("commentLike").setValue("" + (currentCommentLikes - 1))
                            dbRef.child("likedUsers").child("$userId").removeValue()
                            holder.likeButton.setImageResource(R.drawable.blankthumbsup)
                            return
                        }
                    }
                    dbRef.child("commentLike").setValue("" + (currentCommentLikes + 1))
                    dbRef.child("likedUsers").child("$userId").setValue("")
                    holder.likeButton.setImageResource(R.drawable.thumbsup)

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
        val userName: TextView = itemView.findViewById(R.id.namelastname)
        val commentDescription: TextView = itemView.findViewById(R.id.commentdescription)
        val commentLikes: TextView = itemView.findViewById(R.id.commentlikes)
        val commentLogo: ImageView = itemView.findViewById(R.id.commentprofilelogo)
        val likeButton: ImageView = itemView.findViewById(R.id.commentlikebutton)
        val postTime: TextView = itemView.findViewById(R.id.postTime)

    }

}


