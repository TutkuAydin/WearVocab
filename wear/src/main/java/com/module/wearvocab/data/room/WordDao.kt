package com.module.wearvocab.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words_table WHERE isLearned = 0")
    fun getWordsToLearn(): Flow<List<Word>>

    @Query("SELECT * FROM words_table")
    fun getAllWords(): Flow<List<Word>>

    @Query("DELETE FROM words_table")
    suspend fun deleteAllWords()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word)

    @Update
    suspend fun updateWord(word: Word)

    @Delete
    suspend fun deleteWord(word: Word)

}