package com.module.wearvocab.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.wearvocab.data.service.WearableManager
import com.module.wearvocab.data.room.Word
import com.module.wearvocab.data.room.WordDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WordViewModel(
    private val dao: WordDao,
    private val wearableManager: WearableManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WordUiState(isLoading = true))
    val uiState: StateFlow<WordUiState> = _uiState.asStateFlow()

    init {
        handleIntent(WordIntent.LoadWords)
    }

    private fun observeWords() {
        viewModelScope.launch {
            dao.getAllWords()
                .flowOn(Dispatchers.IO)
                .collect { newList ->
                    _uiState.update { it.copy(words = newList, isLoading = false) }
                    wearableManager.sendWordsToWatch(newList.filter { !it.isLearned })
                }
        }
    }

    fun handleIntent(intent: WordIntent) {
        when (intent) {
            is WordIntent.AddWord -> addNewWord(intent)
            is WordIntent.ToggleLearned -> updateWord(intent.word)
            WordIntent.LoadWords -> observeWords()
        }
    }

    private fun addNewWord(intent: WordIntent.AddWord) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertWord(
                Word(
                    englishWord = intent.eng,
                    meaning = intent.tr,
                    exampleSentence = intent.sentence
                )
            )
        }
    }

    private fun updateWord(word: Word) {
        val updatedList = _uiState.value.words.map {
            if (it.id == word.id) it.copy(isLearned = !word.isLearned) else it
        }
        _uiState.update { it.copy(words = updatedList) }

        viewModelScope.launch(Dispatchers.IO) {
            dao.updateWord(word.copy(isLearned = !word.isLearned))
        }
    }
}
