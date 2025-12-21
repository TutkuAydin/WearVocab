package com.module.wearvocab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.module.wearvocab.data.room.WordDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            WordDatabase::class.java, "word-database"
        ).build()

        val dao = db.wordDao()
        val viewModel = WordViewModel(dao)

        setContent {
            WordScreen(viewModel)
        }
    }
}
