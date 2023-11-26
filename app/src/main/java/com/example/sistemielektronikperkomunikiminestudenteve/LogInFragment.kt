package com.example.sistemielektronikperkomunikiminestudenteve

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.sistemielektronikperkomunikiminestudenteve.databinding.FragmentLogInBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
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

