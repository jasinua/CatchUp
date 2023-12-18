package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R

class PostsAdapter (private val idList:ArrayList<GetPostsModel>):
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_posted, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentID = idList[position]
        holder.idpost.text = currentID.PASS
    }

    override fun getItemCount(): Int {
        return idList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idpost: TextView = itemView.findViewById(R.id.PostTitle)
    }


}