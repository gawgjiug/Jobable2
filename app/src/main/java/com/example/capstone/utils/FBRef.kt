package com.example.capstone.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object{
        private val database = Firebase.database

        val boardRef = database.getReference("board")
//        val usersRef = database.getReference("users")
//        val CeousersRef = database.getReference("Ceousers")
    }

}