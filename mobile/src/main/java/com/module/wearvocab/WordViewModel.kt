package com.module.wearvocab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.wearvocab.data.room.Word
import com.module.wearvocab.data.room.WordDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WordViewModel(private val dao: WordDao) : ViewModel() {

    // Tüm kelimeleri otomatik güncellenen bir akış (Flow) olarak alıyoruz
    val allWords: Flow<List<Word>> = dao.getAllWords()

    fun addWord(english: String, meaning: String, sentence: String) {
        viewModelScope.launch {
            dao.insertWord(
                Word(
                    englishWord = english,
                    meaning = meaning,
                    exampleSentence = sentence
                )
            )
        }
    }

    fun toggleLearned(word: Word) {
        viewModelScope.launch {
            dao.updateWord(word.copy(isLearned = !word.isLearned))
        }
    }
}