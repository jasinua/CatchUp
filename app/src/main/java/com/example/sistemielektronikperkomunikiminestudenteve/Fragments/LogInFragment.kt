package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.example.sistemielektronikperkomunikiminestudenteve.databinding.FragmentLogInBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class LogInFragment : Fragment(R.layout.fragment_log_in) {


    private val binding by viewBinding(FragmentLogInBinding::bind)

    private lateinit var navController: NavController



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.logInButton.setOnClickListener {

            navController.navigate(R.id.action_logInFragment_to_homePageFragment2)

        }






    }

}

