package com.example.storytime.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Story
import com.example.domain.usecase.SaveStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StoryUiState(
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val saveStory: SaveStoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoryUiState())
    val uiState: StateFlow<StoryUiState> = _uiState.asStateFlow()

    fun onSave(story: Story) {
        if (_uiState.value.isSaving || _uiState.value.isSaved) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            saveStory(story)
                .onSuccess { _uiState.update { it.copy(isSaving = false, isSaved = true) } }
                .onFailure { e ->
                    _uiState.update { it.copy(isSaving = false, error = e.message ?: "Unknown error") }
                }
        }
    }
}
