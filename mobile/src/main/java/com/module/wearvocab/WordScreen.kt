package com.module.wearvocab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WordScreen(viewModel: WordViewModel) {
    val words by viewModel.allWords.collectAsState(initial = emptyList())

    var eng by remember { mutableStateOf("") }
    var tr by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = eng, onValueChange = { eng = it }, label = { Text(text = "English") })
        TextField(value = tr, onValueChange = { tr = it }, label = { Text("Anlamı") })

        Button(onClick = {
            viewModel.addWord(eng, tr, "Örnek cümle buraya...")
            eng = ""; tr = ""
        }) {
            Text("Ekle")
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn {
            items(words) { word ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = word.englishWord, fontWeight = FontWeight.Bold)
                        Text(text = word.meaning, style = MaterialTheme.typography.bodySmall)
                    }
                    Checkbox(
                        checked = word.isLearned,
                        onCheckedChange = { viewModel.toggleLearned(word) }
                    )
                }
            }
        }
    }
}