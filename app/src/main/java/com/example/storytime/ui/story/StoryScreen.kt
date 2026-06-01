package com.example.storytime.ui.story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Story
import com.example.storytime.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
    story: Story?,
    onBack: () -> Unit,
    viewModel: StoryViewModel = hiltViewModel()
) {
    if (story == null) {
        // Сказка не передана (например, после восстановления процесса) — возвращаемся назад.
        LaunchedEffect(Unit) { onBack() }
        return
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val paragraphs = remember(story.id) {
        story.content
            .split(Regex("\\n\\s*\\n"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .ifEmpty { listOf(story.content.trim()) }
    }

    // Абзацы появляются поочерёдно.
    var visibleCount by remember(story.id) { mutableIntStateOf(0) }
    LaunchedEffect(story.id) {
        for (i in paragraphs.indices) {
            delay(450)
            visibleCount = i + 1
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(story.title.ifBlank { stringResource(R.string.story_title_fallback) }) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                itemsIndexed(paragraphs) { index, paragraph ->
                    AnimatedVisibility(
                        visible = index < visibleCount,
                        enter = fadeIn() + expandVertically()
                    ) {
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.onSave(story) },
                enabled = !uiState.isSaving && !uiState.isSaved,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                when {
                    uiState.isSaving -> Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(22.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    uiState.isSaved -> Text(stringResource(R.string.story_saved))
                    else -> Text(stringResource(R.string.btn_save))
                }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_back))
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
