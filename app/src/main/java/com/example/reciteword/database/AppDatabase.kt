package com.example.reciteword.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reciteword.database.dao.WordDao
import com.example.reciteword.database.entity.Word

@TypeConverters(AppTypeConverters::class)
@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getWordDao(): WordDao
}