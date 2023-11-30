package com.xinhui.quizapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(
    private val tabs:List<Fragment>,
    fragment: Fragment
): FragmentStateAdapter(fragment)
{
    override fun getItemCount() = tabs.size

    override fun createFragment(position: Int): Fragment {
        return tabs[position]
    }
}