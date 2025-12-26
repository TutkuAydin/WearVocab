package com.module.wearvocab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import com.module.wearvocab.data.service.WearableManager
import com.module.wearvocab.data.room.WordDatabase
import com.module.wearvocab.presentation.screen.WordScreen
import com.module.wearvocab.presentation.viewmodel.WordViewModel
import com.module.wearvocab.presentation.viewmodel.WordViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = WordDatabase.getDatabase(this)
        val dao = db.wordDao()
        val wearableManager = WearableManager(applicationContext)

        val viewModelFactory = WordViewModelFactory(dao, wearableManager)
        val viewModel = ViewModelProvider(this, viewModelFactory)[WordViewModel::class.java]

        setContent {
            Surface {
                WordScreen(viewModel)
            }
        }
    }
}
