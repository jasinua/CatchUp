package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    lateinit var id : TextView
    lateinit var mainactivity : MainActivity
    lateinit var idInfo : String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity
        idInfo = mainactivity.getUserId()

        val name = view.findViewById<TextView>(R.id.profileName)
        id = view.findViewById<TextView>(R.id.profileId)
        val drejtimi = view.findViewById<TextView>(R.id.profileDrejtim)
        val email = view.findViewById<TextView>(R.id.profileEmail)
        val passwordchange=view.findViewById<Button>(R.id.changepassword)

        val logout = view.findViewById<Button>(R.id.logOutButton)

        id.text=idInfo

        val dbRef = FirebaseDatabase.getInstance().getReference("USERS")

        passwordchange.setOnClickListener {

        }


        dbRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(snap in snapshot.children){

                    if(snap.key.toString().equals(id.text.toString())){
                        name.text=snap.child("EMRI").getValue().toString()
                        drejtimi.text=snap.child("DREJTIMI").getValue().toString()
                        email.text=snap.child("EMAIL").getValue().toString()
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        logout.setOnClickListener(){
            mainactivity.resetUserInfo()
            mainactivity.setContentView(R.layout.fragment_log_in)
        }
    }

}

