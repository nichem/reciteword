package com.example.worddb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.worddb.database.dao.WordDao
import com.example.worddb.database.entity.Word

@TypeConverters(AppTypeConverters::class)
@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getWordDao(): WordDao
}