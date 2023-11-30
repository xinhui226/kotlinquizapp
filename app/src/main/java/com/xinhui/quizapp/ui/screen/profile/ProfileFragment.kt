package com.xinhui.quizapp.ui.screen.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.FragmentProfileBinding
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import com.xinhui.quizapp.ui.screen.mainActivity.MainActivity
import com.xinhui.quizapp.ui.screen.mainActivity.viewModel.MainActivityViewModel
import com.xinhui.quizapp.ui.screen.profile.viewModel.ProfileViewModelImpl
import com.xinhui.quizapp.ui.screen.signInUp.SignInUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val mainViewModel: MainActivityViewModel by viewModels(
        ownerProducer = { requireActivity() as MainActivity }
    )
    override val viewModel: ProfileViewModelImpl by viewModels()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.updateProfilePic(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        binding.run {
            btnLogout.setOnClickListener {
                mainViewModel.logout()
                val intent = Intent(requireActivity(), SignInUpActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            icEditProfile.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            btnEdit.setOnClickListener {
                viewModel.updateUserDetail(
                    mainViewModel.user.value,
                    etName.text.toString(),
                )
                if (etNewPassword.text.toString().isNotEmpty())
                    viewModel.updateUserPassword(
                        etOldPassword.text.toString(),
                        etNewPassword.text.toString(),
                    )
                mainViewModel.getCurrUser()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            mainViewModel.user.collect{
                binding.etName.setText(it.name)
                binding.tvEmail.text = it.email
            }
        }
        lifecycleScope.launch {
            mainViewModel.profilePic.collect{
                Glide.with(requireContext())
                    .load(it)
                    .placeholder(R.drawable.ic_person)
                    .into(binding.ivProfile)
            }
        }
        lifecycleScope.launch {
            viewModel.finish.collect{
                mainViewModel.getProfileUri()
            }
        }
        lifecycleScope.launch {
            viewModel.success.collect{
                binding.etOldPassword.text = null
                binding.etNewPassword.text = null
            }
        }
    }


}