package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openAfateDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openBinDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openKuizDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openLibraDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openLigjerataDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openProjekteDocument
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.R


class DocumentsFragment(applicationContext : Context) : Fragment(R.layout.fragment_documents) {
    lateinit var mainactivity : MainActivity
    var myContext = applicationContext

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity

        val ligjerata = view.findViewById<ImageButton>(R.id.ligjerata).setOnClickListener { mainactivity.setCurrentFragment(openLigjerataDocument(myContext)) }
        val afate = view.findViewById<ImageButton>(R.id.afate).setOnClickListener { mainactivity.setCurrentFragment(openAfateDocument(myContext))}
        val projekte = view.findViewById<ImageButton>(R.id.projekte).setOnClickListener { mainactivity.setCurrentFragment(openProjekteDocument(myContext)) }
        val kuiz = view.findViewById<ImageButton>(R.id.kuize).setOnClickListener { mainactivity.setCurrentFragment(openKuizDocument(myContext)) }
        val libra = view.findViewById<ImageButton>(R.id.libra).setOnClickListener{ mainactivity.setCurrentFragment(openLibraDocument(myContext)) }
        val bin = view.findViewById<ImageButton>(R.id.bin).setOnClickListener { mainactivity.setCurrentFragment(openBinDocument(myContext)) }


    }

}