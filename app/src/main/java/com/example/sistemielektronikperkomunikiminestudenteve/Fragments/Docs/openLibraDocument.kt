package com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemielektronikperkomunikiminestudenteve.Adapters.DocumentAdapter
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.DocumentsFragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetDocumentsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.math.roundToLong

class openLibraDocument(context: Context) : Fragment(R.layout.fragment_libra_doc) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var docList:ArrayList<GetDocumentsModel>
    private lateinit var databaseReference: Query
    private lateinit var mAdapter : DocumentAdapter

    lateinit var mainactivity : MainActivity
    var myContext = context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val group = "LIBRA"
        recyclerView = view.findViewById(R.id.recyclerViewDocuments)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.setHasFixedSize(true)

        docList = arrayListOf<GetDocumentsModel>()
        mAdapter = DocumentAdapter(docList,myContext)
        recyclerView.adapter = mAdapter


        mainactivity = activity as MainActivity

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("Documents").child(group)

        val listAllTask = storageRef.listAll()

        listAllTask.addOnCompleteListener{ result ->
            val items: List<StorageReference> = result.result!!.items
            items.forEachIndexed{index,item->
                item.downloadUrl.addOnSuccessListener {
                    FirebaseStorage.getInstance().getReferenceFromUrl(it.toString()).metadata.addOnSuccessListener { result ->
                        if(result.sizeBytes!!<1000){
                            docList.add(GetDocumentsModel(it.toString(),(result.sizeBytes!!).toString()+"B",result.contentType.toString()))
                        }else if(result.sizeBytes!!<1000000){
                            docList.add(GetDocumentsModel(it.toString(),(((result.sizeBytes!!/1000.0)* 100.0).roundToLong() / 100.0).toString()+"KB",result.contentType.toString()))
                        }else if(result.sizeBytes!!<1000000000){
                            docList.add(GetDocumentsModel(it.toString(),(((result.sizeBytes!!/1000000.0)* 100.0).roundToLong() / 100.0).toString()+"MB",result.contentType.toString()))
                        }else if(result.sizeBytes!!<1000000000000){
                            docList.add(GetDocumentsModel(it.toString(),(((result.sizeBytes!!/1000000000.0)* 100.0).roundToLong() / 100.0).toString()+"GB",result.contentType.toString()))
                        }
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        val documentPage= DocumentsFragment(myContext)

        val backButton = view.findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener(){
            mainactivity.setCurrentFragment(documentPage)
        }

    }

}