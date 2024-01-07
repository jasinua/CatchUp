package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetDocumentsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.storage.FirebaseStorage

class DocumentAdapter(
    private val items: ArrayList<GetDocumentsModel>, myContext: Context):
    RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {

        var myContext = myContext

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.documentpart, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]

            holder.documentName.text = FirebaseStorage.getInstance().getReferenceFromUrl(item.documentURL.toString()).name
            holder.documentSize.text = item.documentSize

            if(item.documentType.toString().contains("image")){
                holder.documentImage.setImageResource(R.drawable.imagee)
            }else if(item.documentType.toString().contains("pdf")){
                holder.documentImage.setImageResource(R.drawable.pdf)
            }else if(item.documentType.toString().contains("text")){
                holder.documentImage.setImageResource(R.drawable.textfile)
            }

            holder.downloadButton.setOnClickListener(){
                downloadFile(holder.documentName.text.toString(),item.documentURL.toString())
            }

        }

        override fun getItemCount(): Int {
            return items.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val documentImage : ImageView = itemView.findViewById(R.id.documentImage)
            val documentName: TextView = itemView.findViewById(R.id.documentName)
            val downloadButton: ImageView = itemView.findViewById(R.id.downloadButton)
            val documentSize: TextView = itemView.findViewById(R.id.documentSize)
        }

    fun downloadFile(fileName: String, url: String?) {

        val downloadmanager = myContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(
            myContext,
            Environment.DIRECTORY_DOWNLOADS,
            fileName
        )
        downloadmanager.enqueue(request)
    }
    }
