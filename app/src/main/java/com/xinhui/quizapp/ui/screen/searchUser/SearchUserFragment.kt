package com.xinhui.quizapp.ui.screen.searchUser

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhui.quizapp.databinding.FragmentSearchUserBinding
import com.xinhui.quizapp.ui.adapter.AccountAdapter
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import com.xinhui.quizapp.ui.screen.searchUser.viewModel.SearchUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchUserFragment: BaseFragment<FragmentSearchUserBinding>() {

    override val viewModel: SearchUserViewModel by viewModels()
    private val args: SearchUserFragmentArgs by navArgs()
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        setupAccountAdapter()
        viewModel.getGroupInfo(args.groupId)
        binding.run {
            btnSearch.setOnClickListener {
                viewModel.searchAcc(etEmailOrName.text.toString())
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.users.collect{
                accountAdapter.setNewAccountLists(it)
            }
        }
        lifecycleScope.launch {
            viewModel.noAccFound.collect{
                binding.tvNoAccFound.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupAccountAdapter() {
        accountAdapter = AccountAdapter(emptyList()){
            viewModel.addToGroup(it)
        }
        binding.rvStudents.adapter = accountAdapter
        binding.rvStudents.layoutManager = LinearLayoutManager(requireContext())
    }
}