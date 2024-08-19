package com.example.dailymacros.data.database

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

    @Query("SELECT * FROM FoodInsideMeal WHERE mealDate = :mealDate AND mealType = :mealType")
    fun getFoodInsideMeal(mealDate: String, mealType: MealType): Flow<List<FoodInsideMeal>>

    @Upsert
    suspend fun upsert(foodInsideMeal: FoodInsideMeal)

    @Query("DELETE FROM FoodInsideMeal WHERE mealDate = :mealDate AND mealType = :mealType AND foodName = :foodName")
    suspend fun removeFoodInsideMeal(mealDate: String, mealType: MealType, foodName: String)
}

@Dao
interface MealDAO {

    @Query("SELECT * FROM Meal WHERE date = :date")
    fun getMeals(date: String): Flow<List<Meal>>

    @Upsert
    suspend fun upsert(meal: Meal)

    // Delete a meal if is empty (with no food inside)
    @Query("DELETE FROM Meal WHERE date = :date AND type = :mealType")
    suspend fun deleteMeal(date: String, mealType: MealType)

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

        @Upsert
        suspend fun upsert(exerciseInsideDay: ExerciseInsideDay)

        @Query("DELETE FROM ExerciseInsideDay WHERE id = :id")
        suspend fun removeExerciseInsideDay(id: Long)
}
