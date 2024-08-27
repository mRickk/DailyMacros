package com.example.dailymacros.utilities

import kotlin.math.roundToInt

fun calculateBMR(height: Float, weight: Float, age: Int, kGender: Int): Int {
     return ((9.99 * weight) + (6.25 * height) - (4.92 * age) + 5).roundToInt()
}

fun calculateDailyKcal(bmr: Int, kActivity: Float, kGoal: Float): Int {
    return (bmr * kActivity * kGoal).roundToInt()
}
