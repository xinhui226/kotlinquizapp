package com.xinhui.quizapp.ui.screen.signInUp.signIn

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.xinhui.quizapp.R
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.databinding.FragmentSignInBinding
import com.xinhui.quizapp.ui.screen.base.BaseSignInUpFragment
import com.xinhui.quizapp.ui.screen.signInUp.signIn.viewModel.SignInViewModelImpl
import com.xinhui.quizapp.ui.screen.signInUp.signUp.SignUpFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SignInFragment : BaseSignInUpFragment<FragmentSignInBinding>() {
    override val viewModel: SignInViewModelImpl by viewModels()

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        binding.run {
            tvRegister.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.flContainer, SignUpFragment()).commit()
            }
            btnSubmit.setOnClickListener {
                viewModel.signIn(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }
            btnSignInWithGoogle.setOnClickListener {
                signIn()
            }
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModel.getAuth().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = viewModel.getAuth().currentUser
                    viewModel.addUser(
                        User(name = user?.displayName ?: "anonymous",
                            email = user?.email ?: "anonymous",
                            group = emptyList()),
                        user?.photoUrl
                    )
                    user?.photoUrl?.let {
                        Glide.with(requireContext())
                            .downloadOnly()
                            .load(it)
                            .into(object : CustomTarget<File>() {
                                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                                    val imageUri: Uri = Uri.fromFile(resource)
                                    viewModel.saveProfileImg(imageUri)
                                }
                                override fun onLoadCleared(placeholder: Drawable?) {
                                }
                            })
                    }
                } else {
                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}