package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openAfateDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openBinDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openKuizDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openLibraDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openLigjerataDocument
import com.example.sistemielektronikperkomunikiminestudenteve.Fragments.Docs.openProjekteDocument
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.R


class DocumentsFragment : Fragment(R.layout.fragment_documents) {
    lateinit var mainactivity : MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity

        val ligjerata = view.findViewById<ImageButton>(R.id.ligjerata).setOnClickListener { mainactivity.setCurrentFragment(openLigjerataDocument()) }
        val afate = view.findViewById<ImageButton>(R.id.afate).setOnClickListener { mainactivity.setCurrentFragment(openAfateDocument())}
        val projekte = view.findViewById<ImageButton>(R.id.projekte).setOnClickListener { mainactivity.setCurrentFragment(openProjekteDocument()) }
        val kuiz = view.findViewById<ImageButton>(R.id.kuize).setOnClickListener { mainactivity.setCurrentFragment(openKuizDocument()) }
        val libra = view.findViewById<ImageButton>(R.id.libra).setOnClickListener{ mainactivity.setCurrentFragment(openLibraDocument()) }
        val bin = view.findViewById<ImageButton>(R.id.bin).setOnClickListener { mainactivity.setCurrentFragment(openBinDocument()) }


    }

}