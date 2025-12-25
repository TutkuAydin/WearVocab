package com.module.wearvocab.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import com.module.wearvocab.data.room.WordDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val database = WordDatabase.getDatabase(applicationContext)
        val dao = database.wordDao()
        val viewModel = WearWordViewModel(dao)

        setContent {
            MaterialTheme {
                WearWordScreen(viewModel)
            }
        }
    }
}
