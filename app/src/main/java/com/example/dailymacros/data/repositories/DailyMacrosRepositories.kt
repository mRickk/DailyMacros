package com.example.dailymacros.data.repositories

import com.example.dailymacros.data.database.Food
import com.example.dailymacros.data.database.FoodDAO
import com.example.dailymacros.data.database.FoodInsideMeal
import com.example.dailymacros.data.database.FoodInsideMealDAO
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.ExerciseDAO
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.data.database.ExerciseInsideDayDAO
import com.example.dailymacros.data.database.ExerciseInsideDayWithExercise
import com.example.dailymacros.data.database.FoodInsideMealWithFood
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.database.UserDAO
import kotlinx.coroutines.flow.Flow

class DailyMacrosRepository(
    private val userDAO: UserDAO,
    private val foodDAO: FoodDAO,
    private val foodInsideMealDAO: FoodInsideMealDAO,
    private val ExerciseDAO: ExerciseDAO,
    private val ExerciseInsideDayDAO: ExerciseInsideDayDAO
) {

    val users: Flow<List<User>> = userDAO.getAllUsers()

    /* User */
    suspend fun insertUser(user: User) = userDAO.insert(user)
    suspend fun setProfilePicUrl(email: String, pictureUrl: String) = userDAO.setProfilePicUrl(email, pictureUrl)
    suspend fun updateUser(user: User) = userDAO.upsert(user)
    suspend fun login(email: String, password: String) = userDAO.login(email, password)

    /* Food */
    val foods: Flow<List<Food>> = foodDAO.getAllFoods()
    suspend fun upsertFood(food: Food) = foodDAO.upsert(food)
    suspend fun getFood(name: String, email: String) = foodDAO.getFood(name, email)
    suspend fun deleteFood(name: String, email: String) {
        foodDAO.deleteFood(name, email)
        foodInsideMealDAO.removeFoodInsideAllMeals(name, email)
    }

    /* FoodInsideMeal */
    var foodInsideAllMeals: Flow<List<FoodInsideMealWithFood>> = foodInsideMealDAO.getFoodInsideAllMeals()
    suspend fun getFoodsInsideMeal(date: String, mealType: MealType) = foodInsideMealDAO.getFoodInsideMeal(date, mealType)
    suspend fun upsertFoodInsideMeal(foodInsideMeal: FoodInsideMeal) = foodInsideMealDAO.upsert(foodInsideMeal)
    suspend fun deleteFoodsInsideMeal(date: String, mealType: MealType, foodName: String, email: String) = foodInsideMealDAO.removeFoodInsideMeal(date, mealType, foodName, email)

    /* Exercise */
    val exercises: Flow<List<Exercise>> = ExerciseDAO.getAllExercises()
    suspend fun upsertExercise(exercise: Exercise) = ExerciseDAO.upsert(exercise)
    suspend fun getExercise(name: String, email: String) = ExerciseDAO.getExercise(name, email)
    suspend fun deleteExercise(name: String, email: String) {
        ExerciseDAO.deleteExercise(name, email)
        ExerciseInsideDayDAO.removeExerciseInsideAllDays(name, email)
    }

    /* ExerciseInsideDay */
    val exercisesInsideAllDays: Flow<List<ExerciseInsideDayWithExercise>> = ExerciseInsideDayDAO.getExercisesInsideAllDays()
    suspend fun getExercisesInsideDay(date: String) = ExerciseInsideDayDAO.getExercisesInsideDay(date)
    suspend fun upsertExerciseInsideDay(exerciseInsideDay: ExerciseInsideDay) = ExerciseInsideDayDAO.upsert(exerciseInsideDay)
    suspend fun deleteExerciseInsideDay(exerciseInsideDay: ExerciseInsideDay) = ExerciseInsideDayDAO.removeExerciseInsideDay(exerciseInsideDay.exerciseName, exerciseInsideDay.date, exerciseInsideDay.emailEID)

}