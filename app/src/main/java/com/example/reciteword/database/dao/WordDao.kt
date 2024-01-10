package com.example.reciteword.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.reciteword.database.entity.Word

@Dao
interface WordDao {
    @Query("select * from word")
    fun getWords(): List<Word>
}