package com.example.firebase_quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_quiz.model.Employee
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("employees")
    }

    private fun saveEmployee(name: String, jobTitle: String, department: String, email: String) {
        val id = database.push().key
        if (id != null) {
            val employee = Employee(id, name, jobTitle, department, email)
            database.child(id).setValue(employee)
                .addOnSuccessListener {
                    // Success logic
                }
                .addOnFailureListener {
                    // Failure logic
                }
        }
    }
}
