package com.module.wearvocab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.module.wearvocab.data.WearableManager
import com.module.wearvocab.data.room.WordDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            WordDatabase::class.java, "word-database"
        ).build()

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
