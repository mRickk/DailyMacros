package com.example.dailymacros.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val email: String,
    val password: String
)