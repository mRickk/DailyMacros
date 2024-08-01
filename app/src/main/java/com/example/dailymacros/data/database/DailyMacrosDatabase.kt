package com.example.dailymacros.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class], version = 8)
abstract class DailyMacrosDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
}
