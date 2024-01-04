package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.FocusedPost
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetCommentsModel
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetNotificationsModel
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.Date

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
        val dbRef = FirebaseDatabase.getInstance().getReference("POSTS").child(currentID.publicKey.toString())

        dbRef.addListenerForSingleValueEvent(object:ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentComments = snapshot.child("commentSection").childrenCount
                dbRef.child("comments").setValue(currentComments).toString()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

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


        holder.likeButton.setOnClickListener() {
                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val currentLikes = snapshot.child("likes").getValue().toString().toInt()
                                for(snap in snapshot.child("likedUsers").children) {
                                    if(snap.key.toString().equals("$userId")) {
                                        dbRef.child("likes").setValue("" + (currentLikes - 1))
                                        dbRef.child("likedUsers").child("$userId").removeValue()
                                        holder.likeButton.setImageResource(R.drawable.blankthumbsup)
                                        return
                                    }
                                }
                                dbRef.child("likes").setValue("" + (currentLikes + 1))
                                dbRef.child("likedUsers").child("$userId").setValue("")
                                holder.likeButton.setImageResource(R.drawable.thumbsup)


                            ///////////////
                            //notification
                            FirebaseDatabase.getInstance().getReference("USERS").child("$userId").addListenerForSingleValueEvent(object:ValueEventListener{

                                override fun onDataChange(snapshot: DataSnapshot) {

                                    val userName = snapshot.child("EMRI").getValue().toString()
                                    val profileURL = snapshot.child("PROFILE").getValue().toString()
                                    //post time
                                    val timeFormat = SimpleDateFormat("dd/M HH:mm:ss")
                                    val time = timeFormat.format(Date())

                                    //sends notification
                                    FirebaseDatabase.getInstance().getReference("POSTS").child(currentID.publicKey.toString()).addListenerForSingleValueEvent(object:ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val posterid = snapshot.child("posterID").getValue().toString()
                                            if(userId.equals(posterid)){

                                            }else {

                                                val notificationID = FirebaseDatabase.getInstance()
                                                    .getReference("USERS").child(posterid)
                                                    .child("NOTIFICATIONS").push().key!!

                                                val notification = GetNotificationsModel(
                                                    posterid,
                                                    userName,
                                                    currentID.publicKey.toString(),
                                                    "like",
                                                    "$time",
                                                    profileURL,
                                                    false,
                                                    ""
                                                )

                                                FirebaseDatabase.getInstance().getReference("USERS").child(posterid).child("NOTIFICATIONS").addListenerForSingleValueEvent(object:ValueEventListener{
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        for(snap in snapshot.children) {
                                                            if(snap.child("notificationType").getValue().toString().equals("like")
                                                                && snap.child("notificationSenderID").getValue().toString().equals(posterid)
                                                                && snap.child("notificationOfPost").getValue().toString().equals(currentID.publicKey.toString())){
                                                                return
                                                            }
                                                        }

                                                        FirebaseDatabase.getInstance().getReference("USERS")
                                                            .child("$posterid").child("NOTIFICATIONS")
                                                            .child("$notificationID").setValue(notification)
                                                        return

                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                    }

                                                })
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }

                                    })

                                }
                                override fun onCancelled(error: DatabaseError) {
                                }

                            })
                            /////////////////////////////////////////////////////////

                        }
                        override fun onCancelled(error: DatabaseError){
                        }
                })

        }



        holder.commentButton.setOnClickListener() {
            var getCommentText = holder.commentText.text.toString()

                if(holder.commentText.text.toString().trim().equals("")){
                    Toast.makeText(thisContext,"Enter a comment first",Toast.LENGTH_SHORT).show()
                }else {
                var getUserID = userId
                var userName = ""
                var profileURL = ""

                val postID = currentID.publicKey

                //poster name
                FirebaseDatabase.getInstance().getReference("USERS").child("$getUserID")
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {

                            var commentsLike = "0"

                            userName = snapshot.child("EMRI").getValue().toString()
                            profileURL = snapshot.child("PROFILE").getValue().toString()
                            //post id
                            val commentID = FirebaseDatabase.getInstance().getReference("POSTS")
                                .child(postID.toString()).child("commentSection")
                                .push().key!!.toString()


                            //post time
                            val timeFormat = SimpleDateFormat("dd/M HH:mm:ss")
                            val time = timeFormat.format(Date())

                            val comment = GetCommentsModel(
                                getCommentText,
                                commentsLike,
                                profileURL,
                                getUserID,
                                userName,
                                System.currentTimeMillis(),
                                time,
                                commentID
                            )

                            FirebaseDatabase.getInstance().getReference("POSTS")
                                .child(postID.toString()).child("commentSection").child(commentID)
                                .setValue(comment).addOnCompleteListener {
                                    Toast.makeText(thisContext, "Added", Toast.LENGTH_SHORT).show()

                                    //sends notification
                                    FirebaseDatabase.getInstance().getReference("POSTS")
                                        .child("$postID")
                                        .addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                val posterid =
                                                    snapshot.child("posterID").getValue().toString()
                                                if (userId.equals(posterid)) {

                                                } else {
                                                    val notificationID =
                                                        FirebaseDatabase.getInstance()
                                                            .getReference("USERS")
                                                            .child(posterid).child("NOTIFICATIONS")
                                                            .push().key!!

                                                    val notification = GetNotificationsModel(
                                                        posterid,
                                                        userName,
                                                        postID,
                                                        "comment",
                                                        "$time",
                                                        profileURL,
                                                        false,
                                                        getCommentText
                                                    )

                                                    FirebaseDatabase.getInstance()
                                                        .getReference("USERS")
                                                        .child("$posterid").child("NOTIFICATIONS")
                                                        .child("$notificationID")
                                                        .setValue(notification)
                                                }

                                            }


                                            override fun onCancelled(error: DatabaseError) {
                                            }

                                        })

                                    holder.commentText.setText("")

                                }.addOnFailureListener {
                                    Toast.makeText(thisContext, "Failed", Toast.LENGTH_SHORT).show()
                                }


                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })

            }
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
        val postProfile: ImageView = itemView.findViewById(R.id.postProfile)
        val commentLogo : ImageView = itemView.findViewById(R.id.loggedInCommentLogo)
        val commentButton : Button = itemView.findViewById(R.id.postCommentButton)
        val commentText : EditText = itemView.findViewById(R.id.commentText)

    }

}


