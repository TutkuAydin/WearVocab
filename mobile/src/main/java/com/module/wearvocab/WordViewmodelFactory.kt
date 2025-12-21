package com.module.wearvocab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.module.wearvocab.data.WearableManager
import com.module.wearvocab.data.room.WordDao

class WordViewModelFactory(
    private val dao: WordDao,
    private val wearableManager: WearableManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(dao, wearableManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}