package com.example.sistemielektronikperkomunikiminestudenteve

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sistemielektronikperkomunikiminestudenteve.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {


    private lateinit var binding: FragmentLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = FragmentLogInBinding.inflate(layoutInflater)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

}