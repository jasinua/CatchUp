package com.example.sistemielektronikperkomunikiminestudenteve.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sistemielektronikperkomunikiminestudenteve.MainActivity
import com.example.sistemielektronikperkomunikiminestudenteve.Models.GetPostsModel
import com.example.sistemielektronikperkomunikiminestudenteve.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.Date
import java.util.Locale

class postFragment : Fragment(R.layout.fragment_post) {

    private lateinit var title:EditText
    private lateinit var description:EditText
    private lateinit var idInfo : String
    lateinit var mainactivity: MainActivity
    private lateinit var imageButton:ImageView

    lateinit var ImageUri: Uri

    private lateinit var databaseReference:DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity
        idInfo = mainactivity.getUserId()

        title = view.findViewById(R.id.title)
        description = view.findViewById(R.id.postDescription)
        imageButton = view.findViewById(R.id.insertImage)

        imageButton.setOnClickListener {
            selectImage()
        }

        val postProfile = view.findViewById<ImageView>(R.id.postProfile)

        databaseReference = FirebaseDatabase.getInstance().getReference("POSTS")

        view.findViewById<Button>(R.id.postButton).setOnClickListener {
            if(title.text.toString().equals("")){
                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
            }else if(description.text.toString().equals("")){
                Toast.makeText(context, "Please give a description", Toast.LENGTH_SHORT).show()
            }else {
                savePostData()
            }
        }

        FirebaseDatabase.getInstance().getReference("USERS").child("$idInfo").child("PROFILE").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Picasso.with(context).load(snapshot.getValue().toString()).into(postProfile)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
    private fun savePostData() {

        var posttitle = title.text.toString()
        var postdescription = description.text.toString()
        var poster = ""
        var profileURL = ""

        //poster name
        FirebaseDatabase.getInstance().getReference("USERS").child("$idInfo").addListenerForSingleValueEvent(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                poster = snapshot.child("EMRI").getValue().toString()
                profileURL = snapshot.child("PROFILE").getValue().toString()
                //post id
                val postID = databaseReference.push().key!!

                //post time
                val timeFormat = SimpleDateFormat("dd/M HH:mm:ss")
                val time = timeFormat.format(Date())

                val post = GetPostsModel(posttitle, postdescription, poster, profileURL, idInfo, "0" , "0",postID,System.currentTimeMillis(),time)

                databaseReference.child(postID).setValue(post).addOnCompleteListener{
                    mainactivity.setCurrentFragment(FocusedPost(0,postID,poster,posttitle,postdescription,"0","0",time,profileURL))
                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })

    }


    private fun uploadImage(string:String) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Setting profile picture...")
        progressDialog.setCancelable(false)
        progressDialog.show()


        val formatter = java.text.SimpleDateFormat("yyyy_MM_dd_HH_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures/$fileName")
        storageReference.putFile(ImageUri).addOnSuccessListener {

            progressDialog.dismiss()
            Toast.makeText(context, "Sucessfully added image", Toast.LENGTH_SHORT).show()

            val result = it.metadata!!.reference!!.downloadUrl;
            result.addOnSuccessListener {

                var profileURLlink = it.toString()
                FirebaseDatabase.getInstance().getReference("POSTS").child(string).child("PROFILE").setValue("$profileURLlink")

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

        if(resultCode == 100  || resultCode == Activity.RESULT_OK) {
            ImageUri = data?.data!!
//            Toast.makeText(context, "OnActivity", Toast.LENGTH_SHORT).show()
            uploadImage("")
        } else {
//            Toast.makeText(context, "ripActivity", Toast.LENGTH_SHORT).show()
        }
    }

}


