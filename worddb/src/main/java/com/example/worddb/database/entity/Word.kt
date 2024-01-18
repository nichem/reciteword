package com.example.worddb.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey
    val id: Int,
    val bookID: BookID,
    /**
     * 0 没背过 1 背过
     */
    var status: Int,
    val text: String,
    /**
     * 音标
     */
    val usPhonetic: String,
    val ukPhonetic: String,
    /**
     * 音标相对地址，不知道相对哪个网站的
     */
    val usSpeech: String,
    val ukSpeech: String,
    /**
     * 中文释义
     */
    val tranCN: String,
    /**
     * 词性
     */
    val pos: String,
    /**
     * 英文释义
     */
    val tranOther: String,
    /**
     * 选择题题目
     */
    val question: String,
    /**
     * 题目解析
     */
    val explain: String,
    /**
     * 答案索引,1-4,若为负数则代表做过了
     */
    var rightIndex: Int,
    val choiceIndexOne: String,
    val choiceIndexTwo: String,
    val choiceIndexThree: String,
    val choiceIndexFour: String,
    /**
     * 例句
     */
    val sentence: String,
    val sentenceCN: String,
    /**
     * 短语
     */
    val phrase: String,
    val phraseCN: String,
    /**
     * 记忆程度，根据这个去指导下次背的时间
     */
    var rememberLevel: Int = 0,
    /**
     * 下次背的时间
     */
    var nextTime: Long = 0
)
