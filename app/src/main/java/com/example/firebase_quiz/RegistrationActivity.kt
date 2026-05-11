package com.example.firebase_quiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_quiz.databinding.ActivityRegistrationBinding
import com.example.firebase_quiz.model.Complaint
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var db: FirebaseFirestore

    private val categories = arrayOf("IT", "Library", "Transport", "Hostel", "Accounts", "Examination", "Cafeteria", "Administration")
    private val priorities = arrayOf("Low", "Medium", "High", "Urgent")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ComplaintApp", "RegistrationActivity: onCreate")
        
        try {
            binding = ActivityRegistrationBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            db = FirebaseFirestore.getInstance()
            setupSpinners()

            // Using both ViewBinding and findViewById to ensure the click is caught
            val submitBtn: Button = findViewById(R.id.btnSubmit)
            submitBtn.setOnClickListener {
                Log.d("ComplaintApp", "SUBMIT CLICKED!")
                Toast.makeText(this, "Submit Clicked! Processing...", Toast.LENGTH_SHORT).show()
                
                // Change UI state immediately
                binding.tvStatus.text = "Status: Button Pressed"
                submitComplaint()
            }
            
            binding.tvStatus.text = "Status: Ready"
            
        } catch (e: Exception) {
            Log.e("ComplaintApp", "onCreate Error", e)
            showDialog("Error", "Initialization failed: ${e.message}")
        }
    }

    private fun setupSpinners() {
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        val priorityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = priorityAdapter
    }

    private fun submitComplaint() {
        val name = binding.etStudentName.text.toString().trim()
        val roll = binding.etRollNumber.text.toString().trim()
        val title = binding.etComplaintTitle.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem?.toString() ?: "None"
        val priority = binding.spinnerPriority.selectedItem?.toString() ?: "None"
        val description = binding.etComplaintDescription.text.toString().trim()

        if (name.isEmpty() || roll.isEmpty() || title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            binding.tvStatus.text = "Status: Validation Failed"
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSubmit.isEnabled = false
        binding.btnSubmit.text = "SENDING..."
        binding.tvStatus.text = "Status: Sending to Firestore..."

        val complaintRef = db.collection("complaints").document()
        val id = complaintRef.id
        
        val complaint = Complaint(
            id = id,
            studentName = name,
            rollNumber = roll,
            title = title,
            category = category,
            priority = priority,
            description = description,
            status = "Pending",
            timestamp = System.currentTimeMillis()
        )

        Log.d("ComplaintApp", "Starting docRef.set for ID: $id")

        complaintRef.set(complaint)
            .addOnSuccessListener {
                Log.d("ComplaintApp", "Firestore Success!")
                binding.progressBar.visibility = View.GONE
                showDialog("Success", "Complaint saved! ID: $id")
                clearFields()
                resetButton()
            }
            .addOnFailureListener { e ->
                Log.e("ComplaintApp", "Firestore Failure", e)
                binding.progressBar.visibility = View.GONE
                showDialog("Error", "Failed to save: ${e.localizedMessage}")
                resetButton()
            }
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun resetButton() {
        binding.btnSubmit.isEnabled = true
        binding.btnSubmit.text = "SUBMIT COMPLAINT"
    }

    private fun clearFields() {
        binding.etStudentName.text.clear()
        binding.etRollNumber.text.clear()
        binding.etComplaintTitle.text.clear()
        binding.etComplaintDescription.text.clear()
    }
}
