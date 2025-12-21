package com.module.wearvocab.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.wearvocab.data.room.Word
import com.module.wearvocab.data.room.WordDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WearWordViewModel(private val dao: WordDao) : ViewModel() {
    val wordsToLearn: Flow<List<Word>> = dao.getWordsToLearn()

    fun markAsLearned(word: Word) {
        viewModelScope.launch {
            dao.updateWord(word.copy(isLearned = true))
            // TODO: İleride buraya telefona mesaj gönderme kodunu ekleyeceğiz
        }
    }
}