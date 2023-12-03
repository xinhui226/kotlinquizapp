package com.xinhui.quizapp.ui.screen.groupDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.FragmentGroupDetailBinding
import com.xinhui.quizapp.ui.adapter.QuizAdapter
import com.xinhui.quizapp.ui.adapter.StudentAdapter
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupDetailFragment : BaseFragment<FragmentGroupDetailBinding>() {

    override val viewModel: GroupDetailViewModel by viewModels()
    private val args: GroupDetailFragmentArgs by navArgs()
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var quizAdapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser(args.groupId)
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        setupAdapter()
        binding.run {
            btnAddStudent.setOnClickListener {
                val action = GroupDetailFragmentDirections.actionGroupDetailToSearchUser(args.groupId)
                navController.navigate(action)
            }
            ivEdit.setOnClickListener {
                showDialog()
            }
            tvStudent.setOnClickListener {
                (it as TextView).setTextColor(ContextCompat.getColor(requireContext(),R.color.zinc))
                tvQuiz.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray))
                flStudents.visibility = View.VISIBLE
                flGroups.visibility = View.GONE
            }
            tvQuiz.setOnClickListener {
                (it as TextView).setTextColor(ContextCompat.getColor(requireContext(),R.color.zinc))
                tvStudent.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray))
                flGroups.visibility = View.VISIBLE
                flStudents.visibility = View.GONE
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch{
            viewModel.group.collect {
                binding.run {
                tvGroupName.text = it.name
                if (viewModel.user.value.id == it.createdBy){
                    viewModel.getStudents()
                    ivEdit.visibility = View.VISIBLE
                    tvStudent.visibility = View.VISIBLE
                    btnAddStudent.visibility = View.VISIBLE
                }else{
                    tvStudent.visibility = View.GONE
                    btnAddStudent.visibility = View.GONE
                    ivEdit.visibility = View.GONE
                }
                if (it.id != null) viewModel.getQuizByGroup()
                }
            }
        }
        lifecycleScope.launch{
            viewModel.students.collect {
                if(it.isNotEmpty()) binding.tvStudentNoData.visibility = View.GONE
                studentAdapter.setNewStudentLists(it)
            }
        }
        lifecycleScope.launch {
            viewModel.quizzes.collect{
                if(it.isNotEmpty()) binding.tvQuizNoData.visibility = View.GONE
                quizAdapter.setNewQuizzes(it)
            }
        }
        lifecycleScope.launch{
            viewModel.finish.collect {
                viewModel.getGroup(args.groupId)
            }
        }
    }

    private fun setupAdapter() {
        studentAdapter = StudentAdapter(emptyList()){
            viewModel.removeFromGroup(it)
        }
        binding.rvStudents.adapter = studentAdapter
        binding.rvStudents.layoutManager =
            LinearLayoutManager(requireContext())

        quizAdapter = QuizAdapter(emptyList()){
            val action = GroupDetailFragmentDirections.actionGroupDetailToQuizDetail(it.id.toString())
            navController.navigate(action)
        }
        binding.rvQuizzes.adapter = quizAdapter
        binding.rvQuizzes.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        builder.setTitle("Edit Group Name")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.tvGroupName)
        editText.visibility = View.VISIBLE
        editText.setText(viewModel.group.value.name)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { _, _ ->
            viewModel.updateGroupName(editText.text.toString())
        }
        builder.show()
    }
}