package com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.DocumentsFragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class openAfateDocument(context: Context) : Fragment(R.layout.fragment_afate_doc) {
    lateinit var mainactivity : MainActivity
    var myContext = context
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var ref: StorageReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity

        val backButton = view.findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener(){

            download()

            mainactivity.setCurrentFragment(DocumentsFragment(myContext))

        }

    }
    fun download(){
        storageReference = FirebaseStorage.getInstance().getReference()
        ref = storageReference.child("Documents").child("AFATE").child("afati1.txt")


        ref.getDownloadUrl().addOnSuccessListener(){
            val url = it.toString()
            downloadFile(myContext,"File",".txt", DIRECTORY_DOWNLOADS,url)
        }.addOnFailureListener(){

        }
    }

    fun downloadFile(
        context: Context,
        fileName: String,
        fileExtension: String,
        destinationDirectory: String?,
        url: String?
    ) {
        val downloadmanager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(
            context,
            destinationDirectory,
            fileName + fileExtension
        )
        downloadmanager.enqueue(request)
    }

}