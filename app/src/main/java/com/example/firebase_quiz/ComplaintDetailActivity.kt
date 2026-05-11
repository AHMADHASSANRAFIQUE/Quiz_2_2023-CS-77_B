package com.example.firebase_quiz

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_quiz.databinding.ActivityComplaintDetailBinding
import com.example.firebase_quiz.model.Complaint
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ComplaintDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComplaintDetailBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val complaintId = intent.getStringExtra("complaint_id")

        if (complaintId != null) {
            fetchComplaintDetails(complaintId)
        } else {
            Toast.makeText(this, "Error: Complaint ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchComplaintDetails(id: String) {
        db.collection("complaints").document(id).get()
            .addOnSuccessListener { snapshot ->
                val complaint = snapshot.toObject(Complaint::class.java)
                if (complaint != null) {
                    displayDetails(complaint)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching details", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayDetails(complaint: Complaint) {
        binding.apply {
            tvDetailTitle.text = complaint.title
            tvDetailStudentName.text = "Student Name: ${complaint.studentName}"
            tvDetailRollNumber.text = "Roll Number: ${complaint.rollNumber}"
            tvDetailCategory.text = "Category: ${complaint.category}"
            tvDetailPriority.text = "Priority: ${complaint.priority}"
            tvDetailStatus.text = "Status: ${complaint.status}"
            tvDetailDescription.text = complaint.description

            if (complaint.timestamp != 0L) {
                val date = Date(complaint.timestamp)
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                tvDetailDate.text = "Created on: ${sdf.format(date)}"
            } else {
                tvDetailDate.text = "Created on: N/A"
            }
        }
    }
}
