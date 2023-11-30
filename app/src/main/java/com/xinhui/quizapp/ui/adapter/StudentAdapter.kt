package com.xinhui.quizapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.databinding.ItemGroupScoreQuizSimpleLayoutBinding

class StudentAdapter(
    private var students: List<User>,
    private var click: (User)->Unit,
): RecyclerView.Adapter<StudentAdapter.ItemStudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemStudentViewHolder {
        val binding = ItemGroupScoreQuizSimpleLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemStudentViewHolder(binding)
    }

    override fun getItemCount() = students.size

    override fun onBindViewHolder(holder: ItemStudentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    fun setNewStudentLists(students: List<User>){
        this.students = students
        notifyDataSetChanged()
    }

    inner class ItemStudentViewHolder(private val binding: ItemGroupScoreQuizSimpleLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(student: User){
            binding.run{
                tvStudentName.text = student.name
                tvStudentEmail.text = student.email
                llStudent.visibility = View.VISIBLE
                ivRemove.setOnClickListener { click(student) }
            }
        }
    }
}