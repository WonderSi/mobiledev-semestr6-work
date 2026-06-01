package com.example.storytime.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.domain.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel, заскоупленная на навигационный граф (а не на глобальный стейт).
 * Через неё Home/Saved передают выбранную/сгенерированную сказку на StoryScreen.
 */
@HiltViewModel
class StorySessionViewModel @Inject constructor() : ViewModel() {

    var story by mutableStateOf<Story?>(null)
        private set

    fun selectStory(story: Story) {
        this.story = story
    }
}
