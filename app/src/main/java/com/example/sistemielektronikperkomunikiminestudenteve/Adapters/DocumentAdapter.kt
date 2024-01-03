package com.example.sistemielektronikperkomunikiminestudenteve.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetDocumentsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.core.Context

class DocumentAdapter(
    private val idList: ArrayList<GetDocumentsModel>, myContext: Context?):
    RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {

        var thisContext = myContext

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_notification, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentID = idList[position]

            when(currentID.documentType){
                "AFATE" ->
                    print("hello")
                "BIN" ->
                    print("hello")
                "KUIZ" ->
                    print("hello")
                "LIBRA" ->
                    print("hello")
                "LIGJERATA" ->
                    print("hello")
                "PROJEKTE" ->
                    print("hello")
            }
        }

        override fun getItemCount(): Int {
            return idList.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }
    }
