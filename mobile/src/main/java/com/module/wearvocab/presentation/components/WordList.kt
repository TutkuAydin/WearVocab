package com.module.wearvocab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.module.wearvocab.data.room.Word

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordList(
    words: List<Word>,
    onToggleLearned: (Word) -> Unit,
    onDelete: (Word) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = words,
            key = { it.id }
        ) { word ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    when (value) {
                        SwipeToDismissBoxValue.StartToEnd -> {
                            onToggleLearned(word)
                            false
                        }

                        SwipeToDismissBoxValue.EndToStart -> {
                            onDelete(word)
                            true
                        }

                        else -> false
                    }
                }
            )

            LaunchedEffect(word) {
                dismissState.reset()
            }

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val direction = dismissState.dismissDirection
                    val color = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Color(0xFF4CAF50)
                        SwipeToDismissBoxValue.EndToStart -> Color(0xFFF44336)
                        else -> Color.Transparent
                    }
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color, shape = MaterialTheme.shapes.medium)
                            .padding(horizontal = 20.dp),
                        contentAlignment = if (direction == SwipeToDismissBoxValue.StartToEnd)
                            Alignment.CenterStart else Alignment.CenterEnd
                    ) {
                        val icon = if (direction == SwipeToDismissBoxValue.StartToEnd)
                            Icons.Default.Check else Icons.Default.Delete
                        Icon(icon, contentDescription = null, tint = Color.White)
                    }
                }
            ) {
                WordRowItem(word)
            }
        }
    }
}
