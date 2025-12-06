package com.example.bookapp.data.repository

import com.example.bookapp.data.local.dao.UserDao
import com.example.bookapp.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for User data operations
 */
class UserRepository(private val userDao: UserDao) {

    // Get current logged-in user
    val currentUser: Flow<UserEntity?> = userDao.getCurrentUser()

    // Register new user
    suspend fun register(username: String, email: String, password: String): Long {
        val user = UserEntity(
            username = username,
            email = email,
            password = password,
            isLoggedIn = false
        )
        return userDao.insertUser(user)
    }

    // Login user
    suspend fun login(email: String, password: String): UserEntity? {
        val user = userDao.login(email, password)
        if (user != null) {
            userDao.logoutAll() // Logout any other users
            userDao.setLoggedIn(user.id)
        }
        return user
    }

    // Logout current user
    suspend fun logout() {
        userDao.logoutAll()
    }
}