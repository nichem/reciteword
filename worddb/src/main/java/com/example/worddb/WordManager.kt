package com.example.worddb

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.worddb.database.AppDatabase
import com.example.worddb.database.dao.WordDao
import com.example.worddb.database.entity.BookID
import com.example.worddb.database.entity.Word
import com.example.worddb.utils.Common.getNeedReviewDay
import com.example.worddb.utils.Common.getNowDay
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class WordManager(private val context: Context) {

    init {
        MMKV.initialize(context)
    }

    companion object {
        private const val APP_DATABASE_NAME = "tmp.db"
        private const val PAGE_SIZE = 10
    }

    private lateinit var db: AppDatabase
    private lateinit var wordDao: WordDao

    suspend fun getWords(): List<Word> {
        return withContext(IO) {
            wordDao.getWords()
        }
    }


    suspend fun getReciteWords(bookID: BookID?): List<Word> {
        if (bookID == null) return emptyList()
        return withContext(IO) {
            val today = getNowDay()
            val needReview = wordDao.getNeedReviewWords(bookID, today)
            val notRecite = wordDao.getNotReciteWords(bookID)
            Log.d("test", "needReview：${needReview.size} notRecite：${notRecite.size}")
            needReview + notRecite
        }
    }

    suspend fun getNotReciteWords(bookID: BookID?): List<Word> {
        if (bookID == null) return emptyList()
        return withContext(IO) {
            val notRecite = wordDao.getNotReciteWords(bookID)
            Log.d("test", "notRecite：${notRecite.size}")
            notRecite
        }
    }

    suspend fun getNeedReviewWords(bookID: BookID?): List<Word> {
        if (bookID == null) return emptyList()
        return withContext(IO) {
            val today = getNowDay()
            val needReview = wordDao.getNeedReviewWords(bookID, today)
            Log.d("test", "needReview：${needReview.size}")
            needReview
        }
    }

    suspend fun getQuestions(isAnswer: Boolean): List<Word> {
        return withContext(IO) {
            if (isAnswer) wordDao.getAnsweredQuestions()
            else wordDao.getUnAnsweredQuestions()
        }
    }

    suspend fun answerQuestion(word: Word?) {
        if (word == null) return
        withContext(IO) {
            word.rightIndex = -word.rightIndex
            wordDao.update(word)
        }
    }


    suspend fun rememberWord(word: Word) {
        val reviewDay = getNeedReviewDay(word.rememberLevel)
        word.status = 1
        word.rememberLevel++
        word.nextTime = reviewDay
        withContext(IO) {
            wordDao.update(word)
        }
    }

    suspend fun forgetWord(word: Word) {
        val reviewDay = getNeedReviewDay(0)
        word.status = 1
        word.rememberLevel = 1
        word.nextTime = reviewDay
        withContext(IO) {
            wordDao.update(word)
        }
    }

    /**
     * 对某个单词有点印象
     */
    suspend fun normalWord(word: Word) {
        val level = Math.min(word.rememberLevel, 1)
        val reviewDay = getNeedReviewDay(level)
        word.status = 1
        word.rememberLevel = level + 1
        word.nextTime = reviewDay
        withContext(IO) {
            wordDao.update(word)
        }
    }

    suspend fun initDatabase() {
        val dir = File(context.dataDir, "databases").apply {
            if (!exists()) mkdir()
        }
        val fileNames = context.assets.list("")?.filter {
            APP_DATABASE_NAME in it
        } ?: emptyList()
        Log.d("test", "${fileNames.toList()}")
        fileNames.forEach {
            val file = File(dir, it)
            if (file.exists()) return@forEach //continue
            val ins = context.assets.open(it)
            val buffer = ByteArray(2048)
            withContext(IO) {
                val outs = FileOutputStream(file)
                while (true) {
                    val count = ins.read(buffer)
                    if (count < 0) break
                    outs.write(buffer, 0, count)
                }
                outs.flush()
                outs.close()
            }
        }
        db = Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
            .build()
        wordDao = db.getWordDao()
    }

    suspend fun resetDatabase() {
        val dir = File(context.dataDir, "databases").apply {
            if (!exists()) mkdir()
        }
        withContext(IO) {
            dir.listFiles()?.forEach {
                it.delete()
            }
        }
        initDatabase()
    }

    suspend fun getAllRecitedWords(bookID: BookID?): List<Word> {
        if (bookID == null) return emptyList()
        return withContext(IO) { wordDao.getAllRecitedWords(bookID = bookID) }
    }

    suspend fun findRecitedWords(bookID: BookID?, query: String): List<Word> {
        if (bookID == null) return emptyList()
        return withContext(IO) { wordDao.findRecitedWords(bookID, "%$query%") }
    }


    private val mmkv = MMKV.defaultMMKV()
    var currentBookID: BookID
        get() {
            val string = mmkv.decodeString("currentBookID") ?: BookID.CET4_1.toString()
            return BookID.valueOf(string)
        }
        set(value) {
            mmkv.encode("currentBookID", value.toString())
        }

    var skipToday: Long
        get() = mmkv.decodeLong("skipToday")
        set(value) {
            mmkv.encode("skipToday", value)
        }

    fun isSkipTodayReview(): Boolean = skipToday == getNowDay()

}