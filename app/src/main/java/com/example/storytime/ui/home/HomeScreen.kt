package com.example.storytime.ui.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Story
import com.example.domain.model.StoryTheme
import com.example.storytime.R
import com.example.storytime.util.localizedLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStoryGenerated: (Story) -> Unit,
    onOpenSaved: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Одноразовая навигация после успешной генерации.
    LaunchedEffect(uiState.generatedStory) {
        uiState.generatedStory?.let { story ->
            onStoryGenerated(story)
            viewModel.onStoryConsumed()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.home_title)) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.childName,
                onValueChange = viewModel::onNameChange,
                label = { Text(stringResource(R.string.label_child_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.label_theme),
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(StoryTheme.entries) { theme ->
                    FilterChip(
                        selected = uiState.selectedTheme == theme,
                        onClick = { viewModel.onThemeSelect(theme) },
                        label = { Text(theme.localizedLabel()) }
                    )
                }
            }

            GenerateButton(
                isLoading = uiState.isLoading,
                enabled = uiState.canGenerate,
                onClick = viewModel::onGenerate
            )

            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedButton(
                onClick = onOpenSaved,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_saved))
            }
        }
    }
}

@Composable
private fun GenerateButton(
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    // Кнопка «пульсирует» во время загрузки (animateFloatAsState + infiniteRepeatable).
    val scale by animateFloatAsState(
        targetValue = if (isLoading) 1.06f else 1f,
        animationSpec = if (isLoading) {
            infiniteRepeatable(tween(500), RepeatMode.Reverse)
        } else {
            tween(200)
        },
        label = "generateButtonScale"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .scale(scale)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.height(22.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(stringResource(R.string.btn_generate))
        }
    }
}
