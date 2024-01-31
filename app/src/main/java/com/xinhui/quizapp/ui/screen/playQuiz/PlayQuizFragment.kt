package com.xinhui.quizapp.ui.screen.playQuiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.xinhui.quizapp.core.utils.PlayQuiz
import com.xinhui.quizapp.databinding.FragmentPlayQuizBinding
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayQuizFragment : BaseFragment<FragmentPlayQuizBinding>() {
    override val viewModel: PlayQuizViewModel by viewModels()
    private val args: PlayQuizFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuizQuestion(args.quizId)
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.start.collect{
                if (it == 5 ) binding.llCount.visibility = View.VISIBLE
                if (it < 0) {
                    binding.clQuiz.visibility = View.VISIBLE
                    binding.llCount.visibility = View.GONE
                    val play = PlayQuiz(requireContext(),binding,viewModel.quiz.value,{ score->
                        viewModel.addScore(score)
                    }){ navController.popBackStack() }
                    play.initViews()
                }
                else {
                    binding.tvTime.text = it.toString()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.quiz.collect{
                if (it.isPublished) viewModel.startCountdown()
            }
        }
    }
}