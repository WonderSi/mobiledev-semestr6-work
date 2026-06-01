package com.example.storytime.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.storytime.ui.home.HomeScreen
import com.example.storytime.ui.saved.SavedStoriesScreen
import com.example.storytime.ui.story.StoryScreen

object Routes {
    const val GRAPH = "root"
    const val HOME = "home"
    const val STORY = "story"
    const val SAVED = "saved"
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        route = Routes.GRAPH
    ) {
        composable(Routes.HOME) { entry ->
            val session = entry.sharedViewModel<StorySessionViewModel>(navController)
            HomeScreen(
                onStoryGenerated = { story ->
                    session.selectStory(story)
                    navController.navigate(Routes.STORY)
                },
                onOpenSaved = { navController.navigate(Routes.SAVED) }
            )
        }

        composable(Routes.STORY) { entry ->
            val session = entry.sharedViewModel<StorySessionViewModel>(navController)
            StoryScreen(
                story = session.story,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SAVED) { entry ->
            val session = entry.sharedViewModel<StorySessionViewModel>(navController)
            SavedStoriesScreen(
                onOpenStory = { story ->
                    session.selectStory(story)
                    navController.navigate(Routes.STORY)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

/** Достаёт ViewModel, заскоупленную на родительский граф, чтобы шарить её между экранами. */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): T {
    val parentRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) { navController.getBackStackEntry(parentRoute) }
    return hiltViewModel(parentEntry)
}
