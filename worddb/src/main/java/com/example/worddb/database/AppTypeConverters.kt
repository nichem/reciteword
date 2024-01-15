package com.example.worddb.database

import androidx.room.TypeConverter
import com.example.worddb.database.entity.BookID

class AppTypeConverters {
    @TypeConverter
    fun bookId2String(bookID: BookID): String {
        return bookID.toString()
    }

    @TypeConverter
    fun string2BookId(string: String): BookID {
        return BookID.valueOf(string)
    }

}