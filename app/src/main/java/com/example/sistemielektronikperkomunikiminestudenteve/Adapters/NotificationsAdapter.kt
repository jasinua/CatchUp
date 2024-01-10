package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetNotificationsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.squareup.picasso.Picasso

class NotificationsAdapter(
    private val idList: ArrayList<GetNotificationsModel>,
    myContext: Context?
):
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    var thisContext = myContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_notification, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentID = idList[position]

        holder.notificationName.text = currentID.notificationSenderName

        if(currentID.notificationType.equals("like")){
            holder.notificationContext.text = "Liked your post"
        }else if(currentID.notificationType.equals("comment")){
            holder.notificationContext.text = currentID.notificationText.toString()
        }

        holder.notificationTime.text = currentID.notificationTime
        Picasso.with(thisContext).load(currentID.notificationSenderProfileURL).into(holder.notificationprofile)

    }


    override fun getItemCount(): Int {
        return idList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationName = itemView.findViewById<TextView>(R.id.notificationName)
        val notificationContext = itemView.findViewById<TextView>(R.id.notificationContext)
        val notificationTime = itemView.findViewById<TextView>(R.id.notificationTime)
        val notificationprofile = itemView.findViewById<ImageView>(R.id.notificationProfile)

    }

}


