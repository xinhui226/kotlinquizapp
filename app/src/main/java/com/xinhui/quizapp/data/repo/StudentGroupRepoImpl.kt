package com.xinhui.quizapp.data.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xinhui.quizapp.core.service.AuthService
import com.xinhui.quizapp.data.model.StudentGroup
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class StudentGroupRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
):StudentGroupRepo {

    private fun getDBRef(): CollectionReference {
        return db.collection("groups")
    }

    override suspend fun getGroups() = callbackFlow{
        val listener = getDBRef()
            .whereEqualTo("createdBy", authService.getUid())
            .addSnapshotListener { value, error ->
            if(error != null) {
                throw error
            }
            val groups = mutableListOf<StudentGroup>()
            value?.documents?.let { docs ->
                for (doc in docs){
                    doc.data?.let {
                        it["id"] = doc.id
                        groups.add(StudentGroup.fromHash(it))
                    }
                } //end for (doc in docs)
                trySend(groups)
            } //end value?.documents?
        } //end listener = dbref.addSnapshotListener
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun getAllGroups() = callbackFlow{
        val listener = getDBRef()
            .addSnapshotListener { value, error ->
                if(error != null) {
                    throw error
                }
                val groups = mutableListOf<StudentGroup>()
                value?.documents?.let { docs ->
                    for (doc in docs){
                        doc.data?.let {
                            it["id"] = doc.id
                            groups.add(StudentGroup.fromHash(it))
                        }
                    } //end for (doc in docs)
                    trySend(groups)
                } //end value?.documents?
            } //end listener = dbref.addSnapshotListener
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun getGroup(id: String): StudentGroup? {
        val doc = getDBRef().document(id).get().await()
        return doc.data?.let {
            it["id"] = doc.id
            StudentGroup.fromHash(it)
        }
    }

    override suspend fun addNewGroup(name: String) {
        getDBRef().add(
            StudentGroup(
                name=name,
                createdBy = authService.getUid(),
            ).toHash()
        ).await()
    }

    override suspend fun updateStudentGroup(id: String,group: StudentGroup) {
        getDBRef().document(id).set(group.toHash()).await()
    }

    override suspend fun deleteGroup(id: String) {
        getDBRef().document(id).delete().await()
    }

}