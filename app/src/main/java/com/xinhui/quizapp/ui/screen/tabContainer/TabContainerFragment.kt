package com.xinhui.quizapp.ui.screen.tabContainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.FragmentHomeBinding
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
                        tab.text = "Home"
                        tab.setIcon(R.drawable.ic_home)
                    }
                    2-> {
                        tab.text = "Profile"
                        tab.setIcon(R.drawable.ic_person)
                    }
                    else-> {
                        tab.text = "History"
                        tab.setIcon(R.drawable.ic_history)
                    }
                }
            }.attach()
        }
    }

}