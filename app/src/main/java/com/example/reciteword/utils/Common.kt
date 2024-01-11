package com.example.reciteword.utils

object Common {
    fun getNowDay(): Long {
        return System.currentTimeMillis() / (1000 * 24 * 60 * 60)
    }

    private val reviewDays = listOf(1, 2, 4, 7, 15)
    fun getNeedReviewDay(level: Int): Long {
        val nowDay = getNowDay()
        return if (level >= reviewDays.size)
            reviewDays.last() + nowDay
        else reviewDays[level] + nowDay
    }
}