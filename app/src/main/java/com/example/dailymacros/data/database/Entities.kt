package com.example.dailymacros.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val email: String,
    val password: String,
    val username: String,
    val pictureUrl: String?,
    val height: Float,
    val weight: Float,
    val gender: Gender,
    val age: Int,
    val activity: ActivityType,
    val goal: GoalType,
    val bmr: Int,
    val dailyKcal: Int
)

@Entity
data class Food(
    @PrimaryKey val name: String,
    val description: String?,
    val kcalPerc: Float,
    val carbsPerc: Float,
    val fatPerc: Float,
    val proteinPerc: Float,
    val unit: FoodUnit
)

@Entity(primaryKeys = ["foodName", "mealDate", "mealType"])
data class FoodInsideMeal(
    val foodName: String,
    val mealDate: String,
    val mealType: MealType,
    val quantity: Float
)

@Entity(primaryKeys = ["type", "date"])
data class Meal(
    val type: MealType,
    val date: String
)

@Entity
data class Exercise(
    @PrimaryKey val name: String,
    val description: String?,
    val kcalBurnedSec: Float
)

@Entity
data class ExerciseInsideDay(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val exerciseName: String,
    val date: String,
    val duration: Int
)

enum class MealType(val string: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack")
}

enum class Gender(val string: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other")
}

enum class ActivityType(val string: String) {
    SEDENTARY("Sedentary"),
    LIGHTLY_ACTIVE("Lightly Active"),
    MODERATELY_ACTIVE("Moderately Active"),
    VERY_ACTIVE("Very Active"),
    SUPER_ACTIVE("Super Active")
}

enum class GoalType(val string: String) {
    LOSE_WEIGHT("Lose Weight"),
    MAINTAIN_WEIGHT("Maintain Weight"),
    GAIN_WEIGHT("Gain Weight")
}

enum class FoodUnit(val string: String) {
    GRAMS("g"),
    MILLILITERS("ml")
}