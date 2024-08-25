package com.example.dailymacros.utilities

import android.text.format.DateUtils

enum class OverviewPeriods(val string: String, val periodInMillis: Long) {
    WEEK("Last week", 7 * DateUtils.DAY_IN_MILLIS),
    MONTH("Last month", 30 * DateUtils.DAY_IN_MILLIS),
    YEAR("Last year", 365 * DateUtils.DAY_IN_MILLIS)
}