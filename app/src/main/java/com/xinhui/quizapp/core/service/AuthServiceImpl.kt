package com.xinhui.quiz.core.service

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.xinhui.quizapp.core.service.AuthService
import kotlinx.coroutines.tasks.await

class AuthServiceImpl(
    val auth: FirebaseAuth = FirebaseAuth.getInstance(),
): AuthService {

    override fun returnAuth(): FirebaseAuth {
        return auth
    }
    override suspend fun signUp(email: String, password: String): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user
    }

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    override fun getCurrUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getUid(): String {
        return auth.currentUser?.uid ?: throw Exception("No authentic user found")
    }

    override fun updatePassword(
        existPassword: String,
        newPassword: String,
        onFinish: (msg: String, err: String?) -> Unit
    ) {
        val user = auth.currentUser
        if(user?.email != null) {
            user.reauthenticate(
                EmailAuthProvider
                    .getCredential(user.email!!, existPassword)
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) onFinish("Successfully update password.", null)
                            else onFinish("", "Something wrong")
                        }
                } else onFinish("", "Invalid credential")
            }
        } else {
            onFinish("", "No authentic user found")
        }
    }
}