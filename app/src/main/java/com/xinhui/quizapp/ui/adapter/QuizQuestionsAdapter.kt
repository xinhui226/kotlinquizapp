package com.xinhui.quizapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.xinhui.quizapp.data.model.Question
import com.xinhui.quizapp.databinding.ItemQuizLayoutBinding

class QuizQuestionsAdapter(
    private var questions:List<Question>,
):RecyclerView.Adapter<QuizQuestionsAdapter.ItemQuestionViewHolder>() {

    private var newQuess = questions.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemQuestionViewHolder {
        val binding = ItemQuizLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemQuestionViewHolder(binding)
    }

    override fun getItemCount() = questions.size

    override fun onBindViewHolder(holder: ItemQuestionViewHolder, position: Int) {
        holder.bind(questions[position],position)
    }

    fun getLatestQuestions():List<Question>{
        return newQuess
    }

    inner class ItemQuestionViewHolder(private val binding:ItemQuizLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(question:Question,position:Int){
            binding.run{
                tvQuestionNo.text = (position+1).toString()
                etQuestionTitle.setText(question.titles)
                etOption1.setText(question.options[0])
                etOption2.setText(question.options[1])
                etOption3.setText(question.options[2])
                etOption4.setText(question.options[3])
                etQuestionSec.setText(question.seconds.toString())
                when(question.options.indexOf(question.answers)){
                    0 -> radioButton1.isChecked=true
                    1 -> radioButton2.isChecked=true
                    2 -> radioButton3.isChecked=true
                    3 -> radioButton4.isChecked=true
                }
                rbCheckedChange()
                updateQuestions(position)
            }
        }
        private fun updateQuestions(position: Int) {
            binding.run {
                etQuestionTitle.doAfterTextChanged {
                    newQuess[position].titles = it.toString()
                }
                etQuestionSec.doAfterTextChanged {
                    newQuess[position].seconds = it.toString().toLongOrNull() ?: 0
                }
                etOption1.doAfterTextChanged {
                    updateOptionAnswerText(position,0,it.toString())
                }
                etOption2.doAfterTextChanged {
                    updateOptionAnswerText(position,1,it.toString())
                }
                etOption3.doAfterTextChanged {
                    updateOptionAnswerText(position,2,it.toString())
                }
                etOption4.doAfterTextChanged {
                    updateOptionAnswerText(position,3,it.toString())
                }
            }
        }
        private fun updateOptionAnswerText(position: Int,index:Int,string: String){
            val newOptions = newQuess[position].options.toMutableList()
            newOptions[index] = string
            newQuess[position].options = newOptions
            newQuess[position].answers = string
        }
        private fun rbCheckedChange(){
            binding.run {
                radioButton1.setOnClickListener {
                    checkedAndUnchecked(radioButton1.id)
                    newQuess[adapterPosition].answers = etOption1.text.toString()
                }
                radioButton2.setOnClickListener {
                    checkedAndUnchecked(radioButton2.id)
                    newQuess[adapterPosition].answers = etOption2.text.toString()
                }
                radioButton3.setOnClickListener {
                    checkedAndUnchecked(radioButton3.id)
                    newQuess[adapterPosition].answers = etOption3.text.toString()
                }
                radioButton4.setOnClickListener {
                    checkedAndUnchecked(radioButton4.id)
                    newQuess[adapterPosition].answers = etOption4.text.toString()
                }
            }
        }
        private fun checkedAndUnchecked(id:Int){
            binding.run {
                radioButton1.isChecked= radioButton1.id == id
                radioButton2.isChecked= radioButton2.id == id
                radioButton3.isChecked= radioButton3.id == id
                radioButton4.isChecked= radioButton4.id == id
            }
        }
    }
}