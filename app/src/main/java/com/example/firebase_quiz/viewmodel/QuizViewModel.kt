package com.example.firebase_quiz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.firebase_quiz.db.AppDatabase
import com.example.firebase_quiz.model.Question
import com.example.firebase_quiz.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuizRepository
    val questions: LiveData<List<Question>>

    init {
        val questionDao = AppDatabase.getDatabase(application).questionDao()
        repository = QuizRepository(questionDao)
        questions = repository.allQuestions.asLiveData()
    }

    fun refreshData() {
        viewModelScope.launch {
            repository.refreshQuestions()
        }
    }
}
