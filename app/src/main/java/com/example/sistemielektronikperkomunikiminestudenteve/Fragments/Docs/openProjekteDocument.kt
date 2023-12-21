package com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.R
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.DocumentsFragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.google.android.gms.dynamic.SupportFragmentWrapper

class openProjekteDocument : Fragment(R.layout.fragment_projekte_doc) {

    lateinit var mainactivity : MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity

        val documentPage= DocumentsFragment()

        val backButton = view.findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener(){
            mainactivity.setCurrentFragment(documentPage)
        }

    }
}