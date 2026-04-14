package com.example.eduapp1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduapp1.database.User
import com.example.eduapp1.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: GameRepository
) : ViewModel() {

    val users: StateFlow<List<User>> = repository.allUsers
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var username by mutableStateOf("")
        private set

    var selectedLevel by mutableStateOf("1")
        private set

    // This is now the position in the shuffled game order
    var currentQuestionIndex by mutableStateOf(0)
        private set

    var currentScore by mutableStateOf(0)
        private set

    var currentAnswer by mutableStateOf("")
        private set

    var elapsedSeconds by mutableStateOf(0)
        private set

    var gameFinished by mutableStateOf(false)
        private set

    var lastAnswerCorrect by mutableStateOf<Boolean?>(null)
        private set

    var feedbackMessage by mutableStateOf("")
        private set

    var showResultDialog by mutableStateOf(false)
        private set

    var answeredQuestionsCount by mutableStateOf(0)
        private set

    private var shuffledQuestionOrder by mutableStateOf(listOf<Int>())

    fun updateUsername(name: String) {
        username = name
    }

    fun updateLevel(level: String) {
        selectedLevel = level
    }

    fun updateCurrentAnswer(answer: String) {
        currentAnswer = answer
    }

    fun tickTimer() {
        elapsedSeconds++
    }

    fun getTotalQuestions(): Int {
        return repository.getTotalQuestions(selectedLevel)
    }

    fun getCurrentImageIndex(): Int {
        if (shuffledQuestionOrder.isEmpty()) {
            initializeQuestionOrder()
        }
        return shuffledQuestionOrder.getOrElse(currentQuestionIndex) { 0 }
    }

    fun getCurrentCorrectAnswer(): String {
        val actualImageIndex = getCurrentImageIndex()
        return repository.getAnswerForLevelAndIndex(selectedLevel, actualImageIndex)
    }

    fun submitAnswer() {
        val correctAnswer = getCurrentCorrectAnswer()

        answeredQuestionsCount += 1

        if (currentAnswer.trim() == correctAnswer.trim()) {
            currentScore += 10
            lastAnswerCorrect = true
            feedbackMessage = "Correct Answer!"
        } else {
            lastAnswerCorrect = false
            feedbackMessage = "Wrong Answer"
        }

        showResultDialog = true
    }

    fun dismissDialogAndContinue() {
        showResultDialog = false

        val lastIndex = getTotalQuestions() - 1

        if (currentQuestionIndex < lastIndex) {
            currentQuestionIndex++
            currentAnswer = ""
            lastAnswerCorrect = null
            feedbackMessage = ""
        } else {
            gameFinished = true
        }
    }

    fun startNewGame() {
        currentQuestionIndex = 0
        currentScore = 0
        currentAnswer = ""
        elapsedSeconds = 0
        gameFinished = false
        lastAnswerCorrect = null
        feedbackMessage = ""
        showResultDialog = false
        answeredQuestionsCount = 0
        initializeQuestionOrder()
    }

    private fun initializeQuestionOrder() {
        val total = repository.getTotalQuestions(selectedLevel)
        shuffledQuestionOrder = (0 until total).shuffled()
    }

    fun exitGame() {
        gameFinished = false
        showResultDialog = false
    }

    fun resetForReplay() {
        startNewGame()
    }

    fun saveGameResult() {
        if (username.isBlank()) return

        viewModelScope.launch {
            repository.saveResult(
                username = username,
                level = selectedLevel,
                score = currentScore,
                duration = elapsedSeconds
            )
        }
    }

    fun addUser(username: String) {
        viewModelScope.launch {
            repository.addUser(username)
        }
    }

    fun clearUsers() {
        viewModelScope.launch {
            repository.clearUsers()
        }
    }
}
