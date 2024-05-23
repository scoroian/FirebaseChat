package com.scoroian.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Firebase Database con la URL correcta
        val database = FirebaseDatabase.getInstance("https://weatherfirebasechat-default-rtdb.firebaseio.com/")
        val myRef = database.getReference("message")

        // Escribir un mensaje en Firebase Database
        myRef.setValue("Hello, World!")
            .addOnSuccessListener {
                Log.d("Firebase", "Data written successfully!")
            }
            .addOnFailureListener {
                Log.d("Firebase", "Failed to write data")
            }
        myRef.setValue("aaaaa!")
        myRef.setValue("bbbbb")

        // Leer datos desde Firebase Database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("Firebase", "Data read successfully: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Failed to read data", error.toException())
            }
        })
    }
}