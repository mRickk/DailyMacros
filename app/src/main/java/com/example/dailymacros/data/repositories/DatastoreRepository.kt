package com.example.dailymacros.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.dailymacros.data.database.ActivityType
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
                    gender = preferences[GENDER_KEY] ?: "Other",
                    age = preferences[AGE_KEY]?.toInt() ?: 0,
                    activity = ActivityType.valueOf(preferences[ACTIVITY_KEY] ?: "SEDENTARY"),
                    goal = GoalType.valueOf(preferences[GOAL_KEY] ?: "MAINTAIN_WEIGHT"),
                    bmr = preferences[BMR_KEY]?.toInt() ?: 0,
                    dailyKcal = preferences[DAILY_KCAL_KEY]?.toInt() ?: 0
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
            it[EMAIL_KEY] = user.email
            it[PASSWORD_KEY] = user.password
            it[USERNAME_KEY] = user.username
            it[PICTURE_URL_KEY] = user.pictureUrl ?: ""
            it[HEIGHT_KEY] = user.height.toString()
            it[WEIGHT_KEY] = user.weight.toString()
            it[GENDER_KEY] = user.gender
            it[AGE_KEY] = user.age.toString()
            it[ACTIVITY_KEY] = user.activity.name
            it[GOAL_KEY] = user.goal.name
            it[BMR_KEY] = user.bmr.toString()
            it[DAILY_KCAL_KEY] = user.dailyKcal.toString()
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
        }

}

