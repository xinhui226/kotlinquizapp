package com.xinhui.quizapp.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhui.quizapp.databinding.FragmentHomeBinding
import com.xinhui.quizapp.ui.adapter.GroupAdapter
import com.xinhui.quizapp.ui.adapter.QuizAdapter
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import com.xinhui.quizapp.ui.screen.home.viewModel.HomeViewModel
import com.xinhui.quizapp.ui.screen.tabContainer.TabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: QuizAdapter
    private lateinit var groupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        setupAdapter()
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.quizzes.collect{
                if(it.isNotEmpty()) {
                    binding.tvNoData.visibility = View.GONE
                }
                adapter.setNewQuizzes(it)
            }
        }
        lifecycleScope.launch {
            viewModel.groups.collect{
                if(it.isNotEmpty()) binding.tvGrpNoData.visibility = View.GONE
                groupAdapter.setNewGroups(it)
            }
        }
    }

    private fun setupAdapter() {
        adapter = QuizAdapter(emptyList()){
            val action = TabContainerFragmentDirections.actionTabContainerToQuizDetail(it.id.toString())
            navController.navigate(action)
        }
        binding.rvQuizzes.adapter = adapter
        binding.rvQuizzes.layoutManager = LinearLayoutManager(requireContext())

        groupAdapter = GroupAdapter(emptyList()){
            val action = TabContainerFragmentDirections.actionTabContainerToGroupDetail(it)
            navController.navigate(action)
        }
        binding.rvGroups.adapter = groupAdapter
        binding.rvGroups.layoutManager =
            GridLayoutManager(requireContext(),2)
    }
}