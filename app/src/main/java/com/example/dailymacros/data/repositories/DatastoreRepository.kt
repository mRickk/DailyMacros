package com.example.dailymacros.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.dailymacros.data.database.ActivityType
import com.example.dailymacros.data.database.DietType
import com.example.dailymacros.data.database.Gender
import com.example.dailymacros.data.database.GoalType
import com.example.dailymacros.data.database.User
import kotlinx.coroutines.flow.map

class DatastoreRepository (
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PICTURE_URL_KEY = stringPreferencesKey("pictureUrl")
        private val HEIGHT_KEY = stringPreferencesKey("height")
        private val WEIGHT_KEY = stringPreferencesKey("weight")
        private val GENDER_KEY = stringPreferencesKey("gender")
        private val AGE_KEY = stringPreferencesKey("age")
        private val ACTIVITY_KEY = stringPreferencesKey("activity")
        private val GOAL_KEY = stringPreferencesKey("goal")
        private val BMR_KEY = stringPreferencesKey("bmr")
        private val DAILY_KCAL_KEY = stringPreferencesKey("dailyKcal")
        private val DIET_TYPE_KEY = stringPreferencesKey("diet")
        private val B1_KEY = stringPreferencesKey("b1")
        private val B2_KEY = stringPreferencesKey("b2")
        private val B3_KEY = stringPreferencesKey("b3")
        private val B4_KEY = stringPreferencesKey("b4")
        private val B5_KEY = stringPreferencesKey("b5")
        private val B6_KEY = stringPreferencesKey("b6")
        private val SELECTED_DATE_MILLIS_KEY = stringPreferencesKey("selectedDateMillis")
    }

    val user = dataStore.data.map {
        preferences ->
        try {
            if (preferences[EMAIL_KEY] != null) {
                User(
                    email = preferences[EMAIL_KEY] ?: "",
                    password = preferences[PASSWORD_KEY] ?: "",
                    username = preferences[USERNAME_KEY] ?: "",
                    pictureUrl = preferences[PICTURE_URL_KEY]?.takeIf { it.isNotBlank() },
                    height = preferences[HEIGHT_KEY]?.toFloat() ?: 0f,
                    weight = preferences[WEIGHT_KEY]?.toFloat() ?: 0f,
                    gender = Gender.valueOf(preferences[GENDER_KEY] ?: "Other"),
                    age = preferences[AGE_KEY]?.toInt() ?: 0,
                    activity = ActivityType.valueOf(preferences[ACTIVITY_KEY] ?: "Sedentary"),
                    goal = GoalType.valueOf(preferences[GOAL_KEY] ?: "Maintain Weight"),
                    bmr = preferences[BMR_KEY]?.toInt() ?: 0,
                    dailyKcal = preferences[DAILY_KCAL_KEY]?.toInt() ?: 0,
                    diet = DietType.valueOf(preferences[DIET_TYPE_KEY] ?: "Standard"),
                    b1 = preferences[B1_KEY]?.toBoolean() ?: false,
                    b2 = preferences[B2_KEY]?.toBoolean() ?: false,
                    b3 = preferences[B3_KEY]?.toBoolean() ?: false,
                    b4 = preferences[B4_KEY]?.toBoolean() ?: false,
                    b5 = preferences[B5_KEY]?.toBoolean() ?: false,
                    b6 = preferences[B6_KEY]?.toBoolean() ?: false,
                    selectedDateMillis = preferences[SELECTED_DATE_MILLIS_KEY]?.toLong() ?: 0
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("DatastoreRepository", "Error while reading user from DataStore", e)
            null
        }
    }

    suspend fun saveUser(user: User) =
        dataStore.edit {
            Log.v("DatastoreRepository", "Saving user from DataStore")
            it[EMAIL_KEY] = user.email
            it[PASSWORD_KEY] = user.password
            it[USERNAME_KEY] = user.username
            it[PICTURE_URL_KEY] = user.pictureUrl ?: ""
            it[HEIGHT_KEY] = user.height.toString()
            it[WEIGHT_KEY] = user.weight.toString()
            it[GENDER_KEY] = user.gender.name
            it[AGE_KEY] = user.age.toString()
            it[ACTIVITY_KEY] = user.activity.name
            it[GOAL_KEY] = user.goal.name
            it[BMR_KEY] = user.bmr.toString()
            it[DAILY_KCAL_KEY] = user.dailyKcal.toString()
            it[DIET_TYPE_KEY] = user.diet.name
            it[B1_KEY] = user.b1.toString()
            it[B2_KEY] = user.b2.toString()
            it[B3_KEY] = user.b3.toString()
            it[B4_KEY] = user.b4.toString()
            it[B5_KEY] = user.b5.toString()
            it[B6_KEY] = user.b6.toString()
            it[SELECTED_DATE_MILLIS_KEY] = user.selectedDateMillis.toString()
        }

    suspend fun removeUser() =
        dataStore.edit {
            it.remove(EMAIL_KEY)
            it.remove(PASSWORD_KEY)
            it.remove(USERNAME_KEY)
            it.remove(PICTURE_URL_KEY)
            it.remove(HEIGHT_KEY)
            it.remove(WEIGHT_KEY)
            it.remove(GENDER_KEY)
            it.remove(AGE_KEY)
            it.remove(ACTIVITY_KEY)
            it.remove(GOAL_KEY)
            it.remove(BMR_KEY)
            it.remove(DAILY_KCAL_KEY)
            it.remove(DIET_TYPE_KEY)
            it.remove(B1_KEY)
            it.remove(B2_KEY)
            it.remove(B3_KEY)
            it.remove(B4_KEY)
            it.remove(B5_KEY)
            it.remove(B6_KEY)
            it.remove(SELECTED_DATE_MILLIS_KEY)
        }

}
