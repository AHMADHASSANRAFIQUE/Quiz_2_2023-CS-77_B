package com.example.firebase_quiz.repository

import com.example.firebase_quiz.db.QuestionDao
import com.example.firebase_quiz.model.Question
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class QuizRepository(private val questionDao: QuestionDao) {

    val allQuestions: Flow<List<Question>> = questionDao.getAllQuestions()

    suspend fun refreshQuestions() {
        try {
            val db = FirebaseFirestore.getInstance()
            val snapshot = db.collection("questions").get().await()
            val questions = snapshot.toObjects(Question::class.java)
            if (questions.isNotEmpty()) {
                questionDao.deleteAllQuestions()
                questionDao.insertQuestions(questions)
            }
        } catch (e: Exception) {
            // Handle error (e.g., log or notify UI)
        }
    }
}
