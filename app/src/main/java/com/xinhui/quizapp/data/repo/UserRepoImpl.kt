package com.xinhui.quizapp.data.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.data.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
):UserRepo {
    private fun getDBRef():CollectionReference{
        return db.collection("users")
    }

    override suspend fun getUser(): User? {
        val doc = getDBRef().document(authService.getUid()).get().await()
        return doc.data?.let {
            it["id"] = authService.getUid()
            User.fromHash(it)
        }
    }

    override suspend fun addNewUser(user: User) {
        getDBRef().document(authService.getUid()).set(user.toHash()).await()
    }

    override suspend fun updateUserDetail(user: User) {
        getDBRef().document(authService.getUid()).set(user.toHash()).await()
    }

    override suspend fun deleteUser(id: String) {
        getDBRef().document(id).delete().await()
    }

    override suspend fun getStudentsByGroup(group:String) = callbackFlow{
        val listener = getDBRef()
            .whereArrayContains("group", group)
            .addSnapshotListener { value, error ->
            if(error != null) {
                throw error
            }
            val students = mutableListOf<User>()
            value?.documents?.let { docs ->
                for (doc in docs){
                    doc.data?.let {
                        it["id"] = doc.id
                        students.add(User.fromHash(it))
                    }
                }
                trySend(students)
            }
        }
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun searchAcc(keyword: String) = callbackFlow {
        val listener = getDBRef()
            .where(Filter.or(
                Filter.equalTo("email",keyword),
                Filter.equalTo("name",keyword),
            ))
            .addSnapshotListener { value, error ->
                if(error != null) {
                    throw error
                }
                val students = mutableListOf<User>()
                value?.documents?.let { docs ->
                    for (doc in docs){
                        doc.data?.let {
                            it["id"] = doc.id
                            students.add(User.fromHash(it))
                        }
                    }
                    trySend(students)
                }
            }
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun updateUserGroup(id: String,user: User) {
        getDBRef().document(id).set(user.toHash()).await()
    }
}