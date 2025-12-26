package com.module.wearvocab.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.Wearable.*
import com.module.wearvocab.data.room.Word
import com.module.wearvocab.data.room.WordDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WearWordViewModel(private val dao: WordDao) : ViewModel() {

    private val _uiState = MutableStateFlow(WearWordUiState(isLoading = true))
    val uiState: StateFlow<WearWordUiState> = _uiState.asStateFlow()

    init {
        handleIntent(WearWordIntent.LoadWords)
    }

    fun handleIntent(intent: WearWordIntent) {
        when (intent) {
            is WearWordIntent.LoadWords -> observeWords()
            is WearWordIntent.SelectWord -> {
                _uiState.update { it.copy(selectedWord = intent.word) }
            }

            is WearWordIntent.MarkAsLearned -> {
                markAsLearned(intent.word, intent.context)
                _uiState.update { it.copy(selectedWord = null) }
            }
        }
    }

    private fun observeWords() {
        viewModelScope.launch {
            dao.getWordsToLearn()
                .flowOn(Dispatchers.IO)
                .collect { newList ->
                    _uiState.update { it.copy(words = newList, isLoading = false) }
                }
        }
    }

    private fun markAsLearned(word: Word, context: android.content.Context) {
        val currentWords = _uiState.value.words.toMutableList()
        currentWords.remove(word)
        _uiState.update { it.copy(words = currentWords) }
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateWord(word.copy(isLearned = true))
            try {
                val nodes = getNodeClient(context).connectedNodes.await()
                nodes.forEach { node ->
                    getMessageClient(context).sendMessage(
                        node.id,
                        "/word_learned",
                        word.englishWord.toByteArray()
                    ).await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
