package com.example.storytime.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Story
import com.example.domain.usecase.DeleteStoryUseCase
import com.example.domain.usecase.GetSavedStoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavedUiState(
    val stories: List<Story> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class SavedStoriesViewModel @Inject constructor(
    getSavedStories: GetSavedStoriesUseCase,
    private val deleteStory: DeleteStoryUseCase
) : ViewModel() {

    val uiState: StateFlow<SavedUiState> = getSavedStories()
        .map { stories -> SavedUiState(stories = stories, isLoading = false) }
        .onStart { emit(SavedUiState(isLoading = true)) }
        .catch { e -> emit(SavedUiState(isLoading = false, error = e.message ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SavedUiState()
        )

    private val _deleteError = MutableStateFlow<String?>(null)
    val deleteError: StateFlow<String?> = _deleteError.asStateFlow()

    fun onDelete(storyId: String) {
        viewModelScope.launch {
            deleteStory(storyId).onFailure { e ->
                _deleteError.value = e.message ?: "Unknown error"
            }
        }
    }
}
