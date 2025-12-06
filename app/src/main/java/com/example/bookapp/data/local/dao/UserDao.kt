package com.example.bookapp.data.local.dao

import androidx.room.*
import com.example.bookapp.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for User operations
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAll()

    @Query("UPDATE users SET isLoggedIn = 1 WHERE id = :userId")
    suspend fun setLoggedIn(userId: Long)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}