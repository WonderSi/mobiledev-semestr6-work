package com.example.data.firebase

import com.example.data.dto.StoryDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val collection = firestore.collection("stories")

    suspend fun saveStory(storyDto: StoryDto): Result<Unit> = runCatching {
        collection.document(storyDto.id).set(storyDto).await()
    }

    fun getStories(): Flow<List<StoryDto>> = callbackFlow {
        val listener = collection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val stories = snapshot?.toObjects(StoryDto::class.java) ?: emptyList()
                trySend(stories)
            }
        awaitClose { listener.remove() }
    }

    suspend fun deleteStory(storyId: String): Result<Unit> = runCatching {
        collection.document(storyId).delete().await()
    }
}
