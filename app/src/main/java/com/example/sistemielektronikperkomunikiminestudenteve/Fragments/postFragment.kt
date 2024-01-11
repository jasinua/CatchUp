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
import android.widget.TextView
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
    lateinit var fileImageUri : String
    lateinit var postID :String
    lateinit var ImageUri: Uri
    lateinit var postImageFile : ImageView
    lateinit var mainactivity: MainActivity

    private lateinit var databaseReference:DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity
        idInfo = mainactivity.getUserId()

        title = view.findViewById(R.id.title)
        description = view.findViewById(R.id.postDescription)
        postImageFile = view.findViewById(R.id.insertImage)

        val postProfile = view.findViewById<ImageView>(R.id.postProfile)

        databaseReference = FirebaseDatabase.getInstance().getReference("POSTS")
        postID=databaseReference.push().key!!

        view.findViewById<Button>(R.id.postButton).setOnClickListener {
            if(title.text.toString().equals("")){
                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
            }else if(description.text.toString().equals("")){
                Toast.makeText(context, "Please give a description", Toast.LENGTH_SHORT).show()
            }else {
                    savePostData(postID)
            }
        }

        FirebaseDatabase.getInstance().getReference("USERS").child("$idInfo").child("PROFILE").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Picasso.with(context).load(snapshot.getValue().toString()).into(postProfile)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        view.findViewById<TextView>(R.id.insertImageText).setOnClickListener(){
            selectImage()
        }

    }
    private fun savePostData(postID:String) {

        var posttitle = title.text.toString()
        var postdescription = description.text.toString()
        var poster = ""
        var profileURL = ""
        fileImageUri=""
        //poster name
        FirebaseDatabase.getInstance().getReference("USERS").child("$idInfo").addListenerForSingleValueEvent(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                poster = snapshot.child("EMRI").getValue().toString()
                profileURL = snapshot.child("PROFILE").getValue().toString()
                //post id


                //post time
                val timeFormat = SimpleDateFormat("dd/M HH:mm:ss")
                val time = timeFormat.format(Date())

                val post = GetPostsModel(posttitle, postdescription, poster, profileURL, idInfo, "0" , "0",postID,System.currentTimeMillis(),time,fileImageUri)

                databaseReference.child(postID).setValue(post).addOnCompleteListener{
                    mainactivity.setCurrentFragment(FocusedPost(0,postID,poster,posttitle,postdescription,"0","0",time,profileURL,fileImageUri))
                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading image...")
        progressDialog.setCancelable(false)
        progressDialog.show()



        val formatter = java.text.SimpleDateFormat("yyyy_MM_dd_HH_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference =
            FirebaseStorage.getInstance().getReference("PostsFiles/$fileName")


        storageReference.putFile(ImageUri).addOnSuccessListener {

            progressDialog.dismiss()
            Toast.makeText(context, "Sucessfully added image", Toast.LENGTH_SHORT).show()

            val result = it.metadata!!.reference!!.downloadUrl;
            result.addOnSuccessListener {

                fileImageUri = it.toString()
                Toast.makeText(context,"$postID",Toast.LENGTH_SHORT).show()
                FirebaseDatabase.getInstance().getReference("POSTS").child(postID).child("fileUrl")
                    .setValue("$fileImageUri")

            }
        }.addOnFailureListener {
            if (progressDialog.isShowing) {
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
            postImageFile.setImageURI(ImageUri)
//            Toast.makeText(context, "OnActivity", Toast.LENGTH_SHORT).show()
            uploadImage()
        } else {
//            Toast.makeText(context, "ripActivity", Toast.LENGTH_SHORT).show()
        }
    }

}


