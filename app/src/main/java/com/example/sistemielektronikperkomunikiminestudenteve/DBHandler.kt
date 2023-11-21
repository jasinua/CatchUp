package com.example.sistemielektronikperkomunikiminestudenteve

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast



class DBHandler  // creating a constructor for our database handler.
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    // below method is for creating a database by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.


        val createLogin = ("CREATE TABLE " + TABLE_NAME + " ("
                + SID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL + " TEXT, "
                + PASSWORD + " TEXT);")


//        val createPost = ("CREATE TABLE post(" +
//                "    post_id int PRIMARY KEY AUTO_INCREMENT,\n" +
//                "    student_id bigint(13),\n" +
//                "    titulli varchar(80),\n" +
//                "    description text(500),\n" +
//                "    data_postimit date,\n" +
//                "    comment_number int,\n" +
//                "    likes int,\n" +
//                "    FOREIGN KEY(student_id) REFERENCES login(student_id)" +
//                ");")
//
//        val createComment  = ("CREATE TABLE comments (\n" +
//                "\n" +
//                "    comment_id int PRIMARY KEY AUTO_INCREMENT,\n" +
//                "    post_id int,\n" +
//                "    student_id int,\n" +
//                "    comment text(500),\n" +
//                "    like_comment int\n" +
//                "\n" +
//                ");")

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(createLogin)
//        db.execSQL(createPost)
//        db.execSQL(createComment)
    }

    // this method is use to add new course to our sqlite database.
    fun addNewStudent(

//        student_id: Int?,
        email: String?,
        password: String?,
    ) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        val db = this.writableDatabase

        // on below line we are creating a
        // variable for content values.
        val values = ContentValues()

        // on below line we are passing all values
        // along with its key and value pair.

//        values.put(SID, student_id)
        values.put(EMAIL, email)
        values.put(PASSWORD, password)

        // after adding all values we are passing
        // content values to our table.
        var result:Long = db.insert(TABLE_NAME, null, values)

        if (result == (-1).toLong()) {

        }

        // at last we are closing our
        // database after adding database.
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
//        db.execSQL("DROP TABLE IF EXISTS post")
//        db.execSQL("DROP TABLE IF EXISTS comments" )
        onCreate(db)
    }

    companion object {
        // creating a constant variables for our database.
        // below variable is for our database name.
        private const val DB_NAME = "sistem_elektronik-komunikim-studenti.sqlite"

        // below int is our database version
        private const val DB_VERSION = 1

        // below variable is for our table name.
        private const val TABLE_NAME = "login_system"

        // below variable is for our id column.
        private const val SID = "SID"

        // below variable is for our course name column
        private const val EMAIL = "email"

        // below variable id for our course duration column.
        private const val PASSWORD = "password"
    }
}
