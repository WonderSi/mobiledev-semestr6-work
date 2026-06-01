package com.example.storytime.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Story
import com.example.domain.model.StoryTheme
import com.example.domain.usecase.GenerateStoryUseCase
import com.example.storytime.util.appLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val childName: String = "",
    val selectedTheme: StoryTheme? = null,
    val generatedStory: Story? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val canGenerate: Boolean
        get() = childName.isNotBlank() && selectedTheme != null && !isLoading
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val generateStory: GenerateStoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(childName = name, error = null) }
    }

    fun onThemeSelect(theme: StoryTheme) {
        _uiState.update { it.copy(selectedTheme = theme, error = null) }
    }

    fun onGenerate() {
        val state = _uiState.value
        val theme = state.selectedTheme ?: return
        if (state.childName.isBlank() || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            generateStory(state.childName.trim(), theme, appLanguage)
                .onSuccess { story ->
                    _uiState.update { it.copy(isLoading = false, generatedStory = story) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
                }
        }
    }

    /** Сбрасываем «одноразовое» событие навигации после перехода на StoryScreen. */
    fun onStoryConsumed() {
        _uiState.update { it.copy(generatedStory = null) }
    }
}
