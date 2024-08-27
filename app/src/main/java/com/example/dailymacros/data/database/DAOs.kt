package com.example.dailymacros.data.database

import androidx.compose.ui.text.font.FontWeight
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User

    @Query("UPDATE User SET pictureUrl = :pictureUrl WHERE email = :email")
    suspend fun setProfilePicUrl(email: String, pictureUrl: String)

    @Upsert
    suspend fun upsert(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)
}

@Dao
interface FoodDAO {

    @Query("SELECT * FROM Food")
    fun getAllFoods(): Flow<List<Food>>

    @Upsert
    suspend fun upsert(food: Food)

    @Query("SELECT * FROM Food WHERE name = :name AND email = :email")
    suspend fun getFood(name: String, email: String): Food

    @Query("DELETE FROM Food WHERE name = :name AND email = :email")
    suspend fun deleteFood(name: String, email: String)
}

@Dao
interface FoodInsideMealDAO {
    @Query("SELECT FoodInsideMeal.* , Food.* FROM FoodInsideMeal INNER JOIN Food ON FoodInsideMeal.foodName = Food.name WHERE date = :date AND mealType = :mealType")
    fun getFoodInsideMeal(date: String, mealType: MealType): Flow<List<FoodInsideMealWithFood>>

    @Query("SELECT FoodInsideMeal.* , Food.* FROM FoodInsideMeal INNER JOIN Food ON FoodInsideMeal.foodName = Food.name")
    fun getFoodInsideAllMeals(): Flow<List<FoodInsideMealWithFood>>

    @Upsert
    suspend fun upsert(foodInsideMeal: FoodInsideMeal)

    @Query("DELETE FROM FoodInsideMeal WHERE date = :date AND mealType = :mealType AND foodName = :foodName AND emailFIM = :email")
    suspend fun removeFoodInsideMeal(date: String, mealType: MealType, foodName: String, email: String)

    @Query("DELETE FROM FoodInsideMeal WHERE foodName = :foodName AND emailFIM = :email")
    suspend fun removeFoodInsideAllMeals(foodName: String, email: String)
}

@Dao
interface ExerciseDAO {

        @Query("SELECT * FROM Exercise")
        fun getAllExercises(): Flow<List<Exercise>>

        @Upsert
        suspend fun upsert(exercise: Exercise)

        @Query("SELECT * FROM Exercise WHERE name = :name AND email = :email")
        suspend fun getExercise(name: String, email: String): Exercise

        @Query("DELETE FROM Exercise WHERE name = :name AND email = :email")
        suspend fun deleteExercise(name: String, email: String)
}

@Dao
interface ExerciseInsideDayDAO {

        @Query("SELECT * FROM ExerciseInsideDay WHERE date = :date")
        fun getExercisesInsideDay(date: String): Flow<List<ExerciseInsideDay>>

        @Query("SELECT ExerciseInsideDay.*, Exercise.* FROM ExerciseInsideDay INNER JOIN Exercise ON ExerciseInsideDay.exerciseName = Exercise.name")
        fun getExercisesInsideAllDays(): Flow<List<ExerciseInsideDayWithExercise>>

        @Upsert
        suspend fun upsert(exerciseInsideDay: ExerciseInsideDay)

        @Query("DELETE FROM ExerciseInsideDay WHERE exerciseName = :exerciseName AND date = :date AND emailEID = :email")
        suspend fun removeExerciseInsideDay(exerciseName: String, date: String, email: String)

        @Query("DELETE FROM ExerciseInsideDay WHERE exerciseName = :exerciseName AND emailEID = :email")
        suspend fun removeExerciseInsideAllDays(exerciseName: String, email: String)
}
