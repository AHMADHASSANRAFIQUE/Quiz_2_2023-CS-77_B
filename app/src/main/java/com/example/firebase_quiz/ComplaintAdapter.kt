package com.example.firebase_quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_quiz.databinding.ItemComplaintBinding
import com.example.firebase_quiz.model.Complaint

class ComplaintAdapter(
    private var complaints: List<Complaint>,
    private val onItemClick: (Complaint) -> Unit
) : RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    inner class ComplaintViewHolder(private val binding: ItemComplaintBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(complaint: Complaint) {
            binding.tvTitle.text = complaint.title
            binding.tvStudentName.text = "Student: ${complaint.studentName}"
            binding.tvRollNumber.text = "Roll No: ${complaint.rollNumber}"
            binding.tvCategory.text = "Category: ${complaint.category}"
            binding.tvPriority.text = "Priority: ${complaint.priority}"

            binding.root.setOnClickListener {
                onItemClick(complaint)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding = ItemComplaintBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        holder.bind(complaints[position])
    }

    override fun getItemCount(): Int = complaints.size

    fun updateData(newComplaints: List<Complaint>) {
        complaints = newComplaints
        notifyDataSetChanged()
    }
}
