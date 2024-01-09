package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ProfileFragment() : Fragment(R.layout.fragment_profile) {
    lateinit var id : TextView
    lateinit var mainactivity : MainActivity
    lateinit var idInfo : String
    lateinit var ImageUri: Uri
//    lateinit var profileURLlink:String

    lateinit var profile:ImageView
    var changingPass : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        mainactivity = activity as MainActivity
        idInfo = mainactivity.getUserId()

        val name = view.findViewById<TextView>(R.id.profileName)
        id = view.findViewById(R.id.profileId)
        val drejtimi = view.findViewById<TextView>(R.id.profileDrejtim)
        val email = view.findViewById<TextView>(R.id.profileEmail)
        val changePassButton=view.findViewById<Button>(R.id.changePassButton)
        profile= view.findViewById<ImageView>(R.id.profileImage)
        val logout = view.findViewById<Button>(R.id.logOutButton)

        id.text=idInfo

//        profileURLlink = ""

        val dbRef = FirebaseDatabase.getInstance().getReference("USERS")

        dbRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(snap in snapshot.children){

                    if(snap.key.toString().equals(id.text.toString())){
                        name.text=snap.child("EMRI").getValue().toString()
                        drejtimi.text=snap.child("DREJTIMI").getValue().toString()
                        email.text=snap.child("EMAIL").getValue().toString()
                        Picasso.with(context).load(snap.child("PROFILE").getValue().toString()).into(profile)
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        changePassButton.setOnClickListener() {
            changingPass = true
            val passLayout = view.findViewById<FrameLayout>(R.id.passLayout)

            passLayout.visibility = VISIBLE

            val oldPass = passLayout.findViewById<EditText>(R.id.oldPassword)
            val newPass = passLayout.findViewById<EditText>(R.id.newPassword)
            val repeatPass = passLayout.findViewById<EditText>(R.id.repeatPassword)

            val passChangeCancelButton = passLayout.findViewById<Button>(R.id.passChangeCancel)
            val passChangeConfirmButton = passLayout.findViewById<Button>(R.id.passChangeConfirm)

            passChangeCancelButton.setOnClickListener() {
                oldPass.setText("")
                newPass.setText("")
                repeatPass.setText("")
                passLayout.visibility = INVISIBLE
                changingPass = false
            }

            passChangeConfirmButton.setOnClickListener(){
                //min 5 char
                //max 20 char
                dbRef.child(id.text.toString()).addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(!oldPass.text.toString().equals(  snapshot.child("PASS").getValue().toString()  )) {
                            Toast.makeText(context, "Old password doesn't match the current password", Toast.LENGTH_SHORT).show()
                        }else if(newPass.text.toString().contains(" ")){
                            Toast.makeText(context, "Password must not contain empty spaces", Toast.LENGTH_SHORT).show()
                        }else if(newPass.text.toString().length<8){
                            Toast.makeText(context,"New password length must be at least 5 characters",Toast.LENGTH_SHORT).show()
                        }else if(newPass.text.toString().length>20){
                            Toast.makeText(context,"New password length must be less than 20 characters",Toast.LENGTH_SHORT).show()
                        }else if(!newPass.text.toString().equals(repeatPass.text.toString())){
                            Toast.makeText(context,"New password confirmation is incorrect",Toast.LENGTH_SHORT).show()
                        }else if(newPass.text.toString().equals(   snapshot.child("PASS").getValue().toString()    )){
                            Toast.makeText(context,"New password can't be the same as old password",Toast.LENGTH_SHORT).show()
                        }else {
                            Toast.makeText(context,"Password has been changed",Toast.LENGTH_SHORT).show()
                            dbRef.child(id.text.toString()).child("PASS").setValue(newPass.text.toString())

                            oldPass.setText("")
                            newPass.setText("")
                            repeatPass.setText("")

                            passLayout.visibility = INVISIBLE
                            changingPass = false
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }

        }

            logout.setOnClickListener() {
                if (!changingPass) {
                    val editor = mainactivity.returnPref().edit()
                    editor.clear(); //clear all stored data
                    editor.commit();

                    mainactivity.serviceStopper()
                    mainactivity.resetInfo()
                }
            }

        profile.setOnClickListener{
            selectImage()
        }

        view.findViewById<TextView>(R.id.quesetion).setOnClickListener {
            uploadImage()
        }

    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading file...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures/$fileName")
        storageReference.putFile(ImageUri).addOnSuccessListener {

            progressDialog.dismiss()
            Toast.makeText(context, "Sucessfully added image", Toast.LENGTH_SHORT).show()

            val result = it.metadata!!.reference!!.downloadUrl;
            result.addOnSuccessListener {

                var profileURLlink = it.toString()
                Toast.makeText(context, "$profileURLlink", Toast.LENGTH_SHORT).show()
                FirebaseDatabase.getInstance().getReference("USERS").child(id.text.toString()).child("PROFILE").setValue("$profileURLlink")
            }
        } .addOnFailureListener{
            if(progressDialog.isShowing) {
                progressDialog.dismiss()
                Toast.makeText(context, "Failed to upload", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun selectImage() {

        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == 100  || resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            profile.setImageURI(ImageUri)
            Toast.makeText(context, "OnActivity", Toast.LENGTH_SHORT).show()
        } else {

            Toast.makeText(context, "ripActivity", Toast.LENGTH_SHORT).show()
        }
    }
}


