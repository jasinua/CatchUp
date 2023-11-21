package com.example.sistemielektronikperkomunikiminestudenteve

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sistemielektronikperkomunikiminestudenteve.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {


    lateinit var dbHelper:DBHandler

    private lateinit var binding: FragmentLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding  = FragmentLogInBinding.inflate(layoutInflater)

                dbHelper = DBHandler(this.context)
//
//        val db = dbHelper.writableDatabase


        var inputEmail = binding.idInput
        var inputPassword = binding.passwordInput
        var button = binding.logInButton

        var text = binding.logInText

        button.setOnClickListener{

            dbHelper.addNewStudent(inputEmail.text.toString(), inputPassword.text.toString())


        }

        // Inflate the layout for this fragment
        return binding.root
    }

}