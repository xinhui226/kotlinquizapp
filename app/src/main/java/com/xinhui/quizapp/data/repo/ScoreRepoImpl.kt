package com.xinhui.quizapp.data.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.data.model.Score
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ScoreRepoImpl(
    private val authService: AuthService,
    private val userRepo: UserRepo,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): ScoreRepo {

    private fun getDBRef(): CollectionReference {
        return db.collection("scores")
    }

    override suspend fun getGrpUsersScoreByQuiz(isOwner: Boolean, quizId: String) = callbackFlow {
        val user = authService.getCurrUser()?.let {
            userRepo.getUser()
        } ?: throw Exception("No authenticated user found.")
        var query = getDBRef().whereEqualTo("quizId", quizId)

        if (!isOwner) {
            query = query.whereArrayContainsAny("userGroups", user.group)
        }
        val listener = query
            .orderBy("quizScore", Query.Direction.DESCENDING)
            .orderBy(FieldPath.documentId())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    throw error
                }
                val scores = mutableListOf<Score>()
                value?.documents?.let { docs ->
                    for (doc in docs){
                        doc.data?.let {
                            scores.add(Score.fromHash(it))
                        }
                    } //end for (doc in docs)
                    trySend(scores)
                } //end value?.documents?
            } //end listener = dbref.addSnapshotListener
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getUserHistoryQuiz() = callbackFlow {
        val listener = getDBRef().whereEqualTo("userId", authService.getUid())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    throw error
                }
                val quizzes = mutableListOf<String>()
                value?.documents?.let { docs ->
                    for (doc in docs){
                        doc.data?.let {
                            quizzes.add(Score.fromHash(it).quizId)
                        }
                    } //end for (doc in docs)
                    trySend(quizzes)
                } //end value?.documents?
            } //end listener = dbref.addSnapshotListener
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun addScore(score: Score) {
        getDBRef().add(score.toHash()).await()
    }
}