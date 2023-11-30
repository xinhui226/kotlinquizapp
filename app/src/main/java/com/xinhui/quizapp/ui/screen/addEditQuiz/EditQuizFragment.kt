package com.xinhui.quizapp.ui.screen.addEditQuiz

import android.R
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.xinhui.quizapp.ui.screen.addEditQuiz.viewModel.EditQuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditQuizFragment : BaseAddEditQuizFragment() {
    override val viewModel: EditQuizViewModel by viewModels()
    private val args: EditQuizFragmentArgs by navArgs()

    override fun setupUIComponents() {
        super.setupUIComponents()
        viewModel.getQuiz(args.quizId)
        binding.run{
            btnAdd.text = "Update"
            tvSelectGroup.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.darker_gray))
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.quiz.collect {
                selectedDate = it.date
                binding.run {
                    etQuizName.setText(it.name)
                    tvQuizDate.text = it.date
                    spinnerStatus.adapter = ArrayAdapter(requireContext(),
                       R.layout.simple_spinner_item, listOf(it.isPublished,!it.isPublished))
                    setupQuestionsAdapter(it.toQuestions())
                }
            }
        }
        lifecycleScope.launch {
            viewModel.groups.collect{
                val stringBuilder = StringBuilder()
                it.forEachIndexed {i,grp->
                    stringBuilder.append(grp.name)
                    if (i != it.size-1) stringBuilder.append(", ")
                }
                binding.tvSelectGroup.text = stringBuilder.toString()
            }
        }
    }
}