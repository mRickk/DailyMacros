package com.example.dailymacros.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class, Food::class, FoodInsideMeal::class, Exercise::class, ExerciseInsideDay::class], version = 12)
abstract class DailyMacrosDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun foodDAO(): FoodDAO
    abstract fun foodInsideMealDAO(): FoodInsideMealDAO
    abstract fun exerciseDAO(): ExerciseDAO
    abstract fun exerciseInsideDayDAO(): ExerciseInsideDayDAO
}
