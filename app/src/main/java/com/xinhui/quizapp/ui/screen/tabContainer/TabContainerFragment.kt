package com.xinhui.quizapp.ui.screen.tabContainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.FragmentTabContainerBinding
import com.xinhui.quizapp.ui.adapter.FragmentAdapter
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import com.xinhui.quizapp.ui.screen.history.HistoryFragment
import com.xinhui.quizapp.ui.screen.home.HomeFragment
import com.xinhui.quizapp.ui.screen.profile.ProfileFragment
import com.xinhui.quizapp.ui.screen.tabContainer.viewModel.TabContainerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabContainerFragment : BaseFragment<FragmentTabContainerBinding>() {

    override val viewModel: TabContainerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        binding.run {
            vpContainer.adapter = FragmentAdapter(
                listOf(HomeFragment(), HistoryFragment(), ProfileFragment()),
                this@TabContainerFragment)

            TabLayoutMediator(tlTabs,vpContainer){tab,position ->
                when(position){
                    0-> {
                        tab.text = ContextCompat.getString(requireContext(), R.string.home)
                        tab.setIcon(R.drawable.ic_home)
                    }
                    2-> {
                        tab.text = ContextCompat.getString(requireContext(), R.string.profile)
                        tab.setIcon(R.drawable.ic_person)
                    }
                    else-> {
                        tab.text = ContextCompat.getString(requireContext(), R.string.history)
                        tab.setIcon(R.drawable.ic_history)
                    }
                }
            }.attach()
        }
    }

}