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

    @Query("UPDATE User SET weight = :weight WHERE email = :email")
    suspend fun updateWeight(email: String, weight: Float)

    @Query("UPDATE User SET height = :height WHERE email = :email")
    suspend fun updateHeight(email: String, height: Float)

    @Query("UPDATE User SET age = :age WHERE email = :email")
    suspend fun updateAge(email: String, age: Int)

    @Query("UPDATE User SET gender = :gender WHERE email = :email")
    suspend fun updatGender(email: String, gender: Gender)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)
}

@Dao
interface FoodDAO {

    @Query("SELECT * FROM Food")
    fun getAllFoods(): Flow<List<Food>>

    @Upsert
    suspend fun upsert(food: Food)

    @Query("SELECT * FROM Food WHERE name = :name")
    suspend fun getFood(name: String): Food

    @Query("DELETE FROM Food WHERE name = :name")
    suspend fun deleteFood(name: String)
}

@Dao
interface FoodInsideMealDAO {
    @Query("SELECT FoodInsideMeal.* , Food.* FROM FoodInsideMeal INNER JOIN Food ON FoodInsideMeal.foodName = Food.name WHERE date = :date AND mealType = :mealType")
    fun getFoodInsideMeal(date: String, mealType: MealType): Flow<List<FoodInsideMealWithFood>>

    @Query("SELECT FoodInsideMeal.* , Food.* FROM FoodInsideMeal INNER JOIN Food ON FoodInsideMeal.foodName = Food.name")
    fun getFoodInsideAllMeals(): Flow<List<FoodInsideMealWithFood>>

    @Upsert
    suspend fun upsert(foodInsideMeal: FoodInsideMeal)

    @Query("DELETE FROM FoodInsideMeal WHERE date = :date AND mealType = :mealType AND foodName = :foodName")
    suspend fun removeFoodInsideMeal(date: String, mealType: MealType, foodName: String)
}

@Dao
interface ExerciseDAO {

        @Query("SELECT * FROM Exercise")
        fun getAllExercises(): Flow<List<Exercise>>

        @Upsert
        suspend fun upsert(exercise: Exercise)

        @Query("SELECT * FROM Exercise WHERE name = :name")
        suspend fun getExercise(name: String): Exercise

        @Query("DELETE FROM Exercise WHERE name = :name")
        suspend fun deleteExercise(name: String)
}

@Dao
interface ExerciseInsideDayDAO {

        @Query("SELECT * FROM ExerciseInsideDay WHERE date = :date")
        fun getExercisesInsideDay(date: String): Flow<List<ExerciseInsideDay>>

        @Query("SELECT ExerciseInsideDay.*, Exercise.* FROM ExerciseInsideDay INNER JOIN Exercise ON ExerciseInsideDay.exerciseName = Exercise.name")
        fun getExercisesInsideAllDays(): Flow<List<ExerciseInsideDayWithExercise>>

        @Upsert
        suspend fun upsert(exerciseInsideDay: ExerciseInsideDay)

        @Query("DELETE FROM ExerciseInsideDay WHERE id = :id")
        suspend fun removeExerciseInsideDay(id: Long)
}
