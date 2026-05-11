package com.example.firebase_quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase_quiz.databinding.ActivityComplaintListBinding
import com.example.firebase_quiz.model.Complaint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ComplaintListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComplaintListBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: ComplaintAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        fetchComplaints()
    }

    private fun setupRecyclerView() {
        adapter = ComplaintAdapter(emptyList()) { complaint ->
            val intent = Intent(this, ComplaintDetailActivity::class.java)
            intent.putExtra("complaint_id", complaint.id)
            startActivity(intent)
        }
        binding.rvComplaints.layoutManager = LinearLayoutManager(this)
        binding.rvComplaints.adapter = adapter
    }

    private fun fetchComplaints() {
        db.collection("complaints")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val complaintList = snapshot.toObjects(Complaint::class.java)
                    adapter.updateData(complaintList)

                    if (complaintList.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.rvComplaints.visibility = View.GONE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                        binding.rvComplaints.visibility = View.VISIBLE
                    }
                }
            }
    }
}
