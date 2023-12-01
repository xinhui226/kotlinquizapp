package com.xinhui.quizapp.ui.screen.studentGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.FragmentStudentGroupBinding
import com.xinhui.quizapp.ui.adapter.GroupAdapter
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import com.xinhui.quizapp.ui.screen.studentGroup.viewModel.StudentGroupViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentGroupFragment : BaseFragment<FragmentStudentGroupBinding>() {
    override val viewModel: StudentGroupViewModelImpl by viewModels()
    private lateinit var adapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        setupAdapter()
        binding.run {
            btnAdd.setOnClickListener {
                showDialog()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.finish.collect{
                viewModel.getGroups()
            }
        }
        lifecycleScope.launch {
            viewModel.groups.collect{
                if(it.isNotEmpty()) binding.tvNoData.visibility = View.GONE
                adapter.setNewGroups(it)
            }
        }
    }

    private fun setupAdapter() {
        adapter = GroupAdapter(emptyList()){
            val action = StudentGroupFragmentDirections.actionStudentGroupToGroupDetail(it)
            navController.navigate(action)
        }
        binding.rvStudentGroups.adapter = adapter
        binding.rvStudentGroups.layoutManager =
            GridLayoutManager(requireContext(),2)
    }

    private fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        builder.setTitle("Create New Group")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.tvGroupName)
        editText.visibility = View.VISIBLE
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { _, _ ->
            viewModel.addNewGroup(editText.text.toString())
        }
        builder.show()
    }
}