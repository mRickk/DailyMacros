package com.example.dailymacros.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dailymacros.utilities.DateConverter

@Database(entities = [User::class, Food::class, Meal::class, FoodInsideMeal::class], version = 8)
@TypeConverters(DateConverter::class)
abstract class DailyMacrosDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun foodDAO(): FoodDAO
    abstract fun mealDAO(): MealDAO
    abstract fun foodInsideMealDAO(): FoodInsideMealDAO
}
