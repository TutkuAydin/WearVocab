package com.module.wearvocab.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words_table WHERE isLearned = 0")
    fun getWordsToLearn(): Flow<List<Word>>

    @Query("SELECT * FROM words_table")
    fun getAllWords(): Flow<List<Word>>

    @Query("UPDATE words_table SET isLearned = 1 WHERE englishWord = :wordName")
    suspend fun markWordAsLearned(wordName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word)

    @Update
    suspend fun updateWord(word: Word)

    @Delete
    suspend fun deleteWord(word: Word)
}