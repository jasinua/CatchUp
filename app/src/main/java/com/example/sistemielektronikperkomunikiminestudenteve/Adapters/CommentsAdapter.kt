package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetCommentsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.squareup.picasso.Picasso

class CommentsAdapter(
    private val idList: ArrayList<GetCommentsModel>,
    idInfo: String,
    myContext: Context?,
    mainactivity: MainActivity
):
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {



    var thisContext = myContext

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




        fun showToast(message: String) {
            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
        }

    }

}


