package com.example.firebase_quiz.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Complaint(
    var id: String = "",
    var studentName: String = "",
    var rollNumber: String = "",
    var title: String = "",
    var category: String = "",
    var priority: String = "",
    var description: String = "",
    var status: String = "Pending",
    var timestamp: Long = 0L
)
