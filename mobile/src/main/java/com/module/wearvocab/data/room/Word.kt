package com.module.wearvocab.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table")
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val englishWord: String,
    val meaning: String,
    val exampleSentence: String,
    val audioUrl: String? = null,
    val isLearned: Boolean = false
)