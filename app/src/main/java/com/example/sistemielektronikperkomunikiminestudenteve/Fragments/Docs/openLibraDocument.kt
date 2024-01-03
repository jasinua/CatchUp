package com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.DocumentsFragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.R

class openLibraDocument(context: Context) : Fragment(R.layout.fragment_libra_doc) {
    lateinit var mainactivity : MainActivity
    var myContext = context
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity

        val documentPage= DocumentsFragment(myContext)

        val backButton = view.findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener(){
            mainactivity.setCurrentFragment(documentPage)
        }

    }

}