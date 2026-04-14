package com.example.eduapp1.repository

import com.example.eduapp1.database.AppDao
import com.example.eduapp1.database.User
import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: AppDao) {

    val allUsers: Flow<List<User>> = dao.getAllUsers()

    suspend fun saveResult(
        username: String,
        level: String,
        score: Int,
        duration: Int
    ) {
        dao.insert(
            User(
                username = username,
                level = level,
                score = score,
                duration = duration
            )
        )
    }

    suspend fun addUser(username: String) {
        dao.insert(User(username = username))
    }

    suspend fun clearUsers() {
        dao.deleteAll()
    }

    fun getAnswersForLevel(level: String): List<String> {
        return when (level) {
            "1" -> listOf("0", "21", "15", "55", "6", "0")
            "2" -> listOf("31", "26", "4", "2", "35", "63")
            "3" -> listOf("27", "4", "5", "24", "25", "4")
            else -> listOf("0", "21", "15", "55", "6", "0")
        }
    }

    fun getAnswerForLevelAndIndex(level: String, index: Int): String {
        return getAnswersForLevel(level).getOrElse(index) { "" }
    }

    fun getTotalQuestions(level: String): Int {
        return getAnswersForLevel(level).size
    }
}
