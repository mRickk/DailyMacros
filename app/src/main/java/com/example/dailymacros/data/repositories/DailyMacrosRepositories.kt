package com.example.dailymacros.data.repositories

import com.example.dailymacros.data.database.Food
import com.example.dailymacros.data.database.FoodDAO
import com.example.dailymacros.data.database.FoodInsideMeal
import com.example.dailymacros.data.database.FoodInsideMealDAO
import com.example.dailymacros.data.database.Meal
import com.example.dailymacros.data.database.MealDAO
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.ExerciseDAO
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.data.database.ExerciseInsideDayDAO
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.database.UserDAO
import kotlinx.coroutines.flow.Flow

class DailyMacrosRepository(
    private val userDAO: UserDAO,
    private val foodDAO: FoodDAO,
    private val foodInsideMealDAO: FoodInsideMealDAO,
    private val MealDAO: MealDAO,
    private val ExerciseDAO: ExerciseDAO,
    private val ExerciseInsideDayDAO: ExerciseInsideDayDAO
) {

    val users: Flow<List<User>> = userDAO.getAllUsers()

    /* User */
    suspend fun insertUser(user: User) = userDAO.insert(user)

    suspend fun login(email: String, password: String) = userDAO.login(email, password)

    /* Food */
    val foods: Flow<List<Food>> = foodDAO.getAllFoods()
    suspend fun upsertFood(food: Food) = foodDAO.upsert(food)
    suspend fun getFood(name: String) = foodDAO.getFood(name)
    suspend fun deleteFood(name: String) = foodDAO.deleteFood(name)

    /* Meal */
    suspend fun getMeals(date: String) = MealDAO.getMeals(date)
    suspend fun upsertMeal(meal: Meal) = MealDAO.upsert(meal)
    suspend fun deleteMeal(date: String, mealType: MealType) = MealDAO.deleteMeal(date, mealType)

    /* FoodInsideMeal */
    suspend fun getFoodsInsideMeal(meal: Meal) = foodInsideMealDAO.getFoodInsideMeal(meal.date, meal.type)
    suspend fun upsertFoodInsideMeal(foodInsideMeal: FoodInsideMeal) = foodInsideMealDAO.upsert(foodInsideMeal)
    suspend fun deleteFoodsInsideMeal(meal: Meal, food: Food) = foodInsideMealDAO.removeFoodInsideMeal(meal.date, meal.type, food.name)

    /* Exercise */
    val exercises: Flow<List<Exercise>> = ExerciseDAO.getAllExercises()
    suspend fun upsertExercise(exercise: Exercise) = ExerciseDAO.upsert(exercise)
    suspend fun getExercise(name: String) = ExerciseDAO.getExercise(name)
    suspend fun deleteSport(name: String) = ExerciseDAO.deleteExercise(name)

    /* ExerciseInsideDay */
    suspend fun getExercisesInsideDay(date: String) = ExerciseInsideDayDAO.getExercisesInsideDay(date)
    suspend fun upsertExerciseInsideDay(exerciseInsideDay: ExerciseInsideDay) = ExerciseInsideDayDAO.upsert(exerciseInsideDay)
    suspend fun deleteExerciseInsideDay(exerciseInsideDay: ExerciseInsideDay) = ExerciseInsideDayDAO.removeExerciseInsideDay(exerciseInsideDay.id)

}