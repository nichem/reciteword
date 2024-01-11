package com.example.reciteword.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.reciteword.database.entity.BookID
import com.example.reciteword.database.entity.Word

@Dao
interface WordDao {
    @Query("select * from word")
    fun getWords(): List<Word>

    @Query("select * from word where bookID=:bookID and status=0")
    fun getNotReciteWords(bookID: BookID): List<Word>

    /**
     * 获取需要复习的单词
     */
    @Query("select * from word where bookID=:bookID and status=1 and nextTime < :today order by nextTime")
    fun getNeedReviewWords(
        bookID: BookID,
        today: Long
    ): List<Word>

    @Update
    fun update(word: Word)
}