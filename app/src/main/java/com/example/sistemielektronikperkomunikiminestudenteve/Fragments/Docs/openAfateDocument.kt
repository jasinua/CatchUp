package com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.DocumentsFragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class openAfateDocument(context: Context) : Fragment(R.layout.fragment_afate_doc) {
    lateinit var mainactivity : MainActivity
    var myContext = context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity

        val documentPage= DocumentsFragment(myContext)

        val backButton = view.findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener(){

            val storageRef = FirebaseStorage.getInstance().getReference("Documents")
            val islandRef = storageRef.child("AFATE/afati1.txt")

            val localFile = File.createTempFile("afati1", "txt")

            islandRef.getFile(localFile).addOnSuccessListener {
                Toast.makeText(myContext,"Download complete", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // Handle any errors
            }

            mainactivity.setCurrentFragment(documentPage)
        }



    }
}