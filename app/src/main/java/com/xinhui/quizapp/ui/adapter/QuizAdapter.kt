package com.xinhui.quizapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.databinding.ItemGroupScoreQuizSimpleLayoutBinding

class QuizAdapter(
    private var quizzes: List<Quiz>,
    private val onClick:(Quiz)->Unit
): RecyclerView.Adapter<QuizAdapter.ItemQuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemQuizViewHolder {
        val binding = ItemGroupScoreQuizSimpleLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemQuizViewHolder(binding)
    }

    override fun getItemCount() = quizzes.size

    override fun onBindViewHolder(holder: ItemQuizViewHolder, position: Int) {
        holder.bind(quizzes[position])
    }

    fun setNewQuizzes(quizzes: List<Quiz>){
        this.quizzes = quizzes
        notifyDataSetChanged()
    }

    inner class ItemQuizViewHolder(private val binding:ItemGroupScoreQuizSimpleLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz:Quiz){
            binding.run{
                tvQuizName.text = quiz.name
                tvQuizDate.text = quiz.date
                llQuiz.setOnClickListener { onClick(quiz) }
                llQuiz.visibility= View.VISIBLE
            }
        }
    }
}