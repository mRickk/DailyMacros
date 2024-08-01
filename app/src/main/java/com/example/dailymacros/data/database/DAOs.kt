package com.example.dailymacros.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)
}