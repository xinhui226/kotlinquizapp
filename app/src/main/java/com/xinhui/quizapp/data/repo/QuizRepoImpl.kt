package com.xinhui.quizapp.data.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuizRepoImpl(
    private val authService: AuthService,
    private val userRepo: UserRepo,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
):QuizRepo {

    private fun getDBRef(): CollectionReference {
        return db.collection("quizzes")
    }

    override suspend fun getOwnQuizzes() = callbackFlow{
        val listener =
            getDBRef()
                .whereEqualTo("createdBy", authService.getUid())
                .orderBy("date")
                .addSnapshotListener { value, error ->
                    if(error != null) {
                        throw error
                    }
                    val quizzes = mutableListOf<Quiz>()
                    value?.documents?.let { docs ->
                        for (doc in docs){
                            doc.data?.let {
                                it["id"] = doc.id
                                quizzes.add(Quiz.fromHash(it))
                            }
                        } //end for (doc in docs)
                        trySend(quizzes)
                    } //end value?.documents?
                } //end listener = query.addSnapshotListener
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun getGrpQuiz() = callbackFlow{
        val listener =
            userRepo.getUser()?.group?.let {groups ->
                getDBRef()
                    .whereEqualTo("isPublished",true)
                    .whereArrayContainsAny("groups", groups)
                    .orderBy("date")
                    .addSnapshotListener { value, error ->
                        if(error != null) {
                            throw error
                        }
                        val quizzes = mutableListOf<Quiz>()
                        value?.documents?.let { docs ->
                            for (doc in docs){
                                doc.data?.let {
                                    it["id"] = doc.id
                                    quizzes.add(Quiz.fromHash(it))
                                }
                            } //end for (doc in docs)
                            trySend(quizzes)
                        } //end value?.documents?
                    }
            } //end listener = query.addSnapshotListener
        awaitClose{
            listener?.remove()
        }
    }

    override suspend fun getQuiz(id: String): Quiz? {
        val doc = getDBRef().document(id).get().await()
        return doc.data?.let {
            it["id"] = doc.id
            Quiz.fromHash(it)
        }
    }

    override suspend fun addNewQuiz(quiz: Quiz) {
        getDBRef().add(
            Quiz(name=quiz.name,
                createdBy = authService.getUid(),
                date = quiz.date,
                titles = quiz.titles,
                options = quiz.options,
                answers = quiz.answers,
                seconds = quiz.seconds,
                groups = quiz.groups,
                isPublished = quiz.isPublished
            ).toHash()
        ).await()
    }

    override suspend fun updateQuiz(id: String, quiz: Quiz) {
        getDBRef().document(id).set(
            Quiz(name=quiz.name,
                createdBy = quiz.createdBy,
                date = quiz.date,
                titles = quiz.titles,
                options = quiz.options,
                answers = quiz.answers,
                seconds = quiz.seconds,
                groups = quiz.groups,
                isPublished = quiz.isPublished
            ).toHash()).await()
    }

    override suspend fun deleteQuiz(id: String) {
        getDBRef().document(id).delete().await()
    }
}