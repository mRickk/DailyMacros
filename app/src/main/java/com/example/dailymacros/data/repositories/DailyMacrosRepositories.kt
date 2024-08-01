package com.example.dailymacros.data.repositories

import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.database.UserDAO
import kotlinx.coroutines.flow.Flow

class PokExploreRepository(
    private val userDAO: UserDAO,
) {

    val users: Flow<List<User>> = userDAO.getAllUsers()

    /* User */
    suspend fun insertUser(user: User) = userDAO.insert(user)

    suspend fun login(email: String, password: String) = userDAO.login(email, password)
}