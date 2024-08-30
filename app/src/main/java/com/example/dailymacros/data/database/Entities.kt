package com.example.dailymacros.data.database

import androidx.room.Embedded
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
    val dailyKcal: Int,
    val diet: DietType,
    var b1: Boolean = false,
    var b2: Boolean = false,
    var b3: Boolean = false,
    var b4: Boolean = false,
    var b5: Boolean = false,
    var b6: Boolean = false
)

@Entity(primaryKeys = ["name", "email"])
data class Food(
    val email: String,
    val name: String,
    val description: String?,
    val kcalPerc: Float,
    val carbsPerc: Float,
    val fatPerc: Float,
    val proteinPerc: Float,
    val unit: FoodUnit,
    var isFavourite: Boolean
)

@Entity(primaryKeys = ["emailFIM", "foodName", "date", "mealType"])
data class FoodInsideMeal(
    val emailFIM: String,
    val foodName: String,
    val date: String,
    val mealType: MealType,
    val quantity: Float
)

@Entity(primaryKeys = ["email", "name"])
data class Exercise(
    val email: String,
    val name: String,
    val description: String?,
    val kcalBurnedSec: Float,
    var isFavourite: Boolean
)

@Entity(primaryKeys = ["emailEID","exerciseName", "date"])
data class ExerciseInsideDay(
    val emailEID: String,
    val exerciseName: String,
    val date: String,
    val duration: Int
)

data class FoodInsideMealWithFood(
    @Embedded val foodInsideMeal: FoodInsideMeal,
    @Embedded val food: Food
)

data class ExerciseInsideDayWithExercise(
    @Embedded val exerciseInsideDay: ExerciseInsideDay,
    @Embedded val exercise: Exercise
)

enum class MealType(val string: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack")
}

enum class Gender(val string: String, val k: Int) {
    MALE("Male", 5),
    FEMALE("Female", -161),
    OTHER("Other", -78)
}

enum class ActivityType(val string: String, val description: String, val k: Float) {
    SEDENTARY("Sedentary", "(little to no exercise)", 1.2f),
    LIGHTLY_ACTIVE("Lightly Active", "(1-3 days per week)", 1.375f),
    MODERATELY_ACTIVE("Moderately Active", "(3-5 days per week)", 1.55f),
    VERY_ACTIVE("Very Active", "(6-7 days per week)", 1.725f),
    SUPER_ACTIVE("Super Active", "(twice per day, extra heavy workouts)", 2f)
}

enum class GoalType(val string: String, val     k: Float) {
    LOSE_WEIGHT("Lose Weight", 0.8f),
    LOSE_WEIGHT_SLOWLY("Lose Weight Slowly", 0.9f),
    MAINTAIN_WEIGHT("Maintain Weight", 1f),
    GAIN_WEIGHT_SLOWLY("Gain Weight Slowly", 1.1f),
    GAIN_WEIGHT("Gain Weight", 1.2f)
}

enum class FoodUnit(val string: String) {
    GRAMS("g"),
    MILLILITERS("ml")
}

enum class DietType(val string: String, val carbsPerc: Float, val proteinPerc: Float, val fatPerc: Float) {
    STANDARD("Standard", 0.50f, 0.20f, 0.30f),
    BALANCED("Balanced", 0.50f, 0.25f, 0.25f),
    LOW_FAT("Low Fat", 0.60f, 0.25f, 0.15f),
    HIGH_PROTEIN("High Protein", 0.25f, 0.40f, 0.35f),
    KETOGENIC("Ketogenic", 0.05f, 0.30f, 0.65f)
}