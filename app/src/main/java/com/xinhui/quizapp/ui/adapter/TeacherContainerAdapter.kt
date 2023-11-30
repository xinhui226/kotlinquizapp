package com.xinhui.quizapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.ItemFragmentLayoutBinding

class TeacherContainerAdapter(
    private var fragmentsId:List<Int>,
    private val onClick:(Int)->Unit
): RecyclerView.Adapter<TeacherContainerAdapter.ItemFragmentViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherContainerAdapter.ItemFragmentViewHolder {
        val binding = ItemFragmentLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemFragmentViewHolder(binding)
    }

    override fun getItemCount() = fragmentsId.size

    override fun onBindViewHolder(
        holder: TeacherContainerAdapter.ItemFragmentViewHolder,
        position: Int) {
        holder.bind(fragmentsId[position])
    }

    inner class ItemFragmentViewHolder(
        private val binding: ItemFragmentLayoutBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(id:Int){
            binding.run {
                when(id){
                    R.id.studentGroupFragment ->{
                        ivIcon.setImageResource(R.drawable.ic_people)
                        tvFragmentName.text= ContextCompat.getString(binding.root.context,R.string.group)
                    }
                    R.id.manageQuizFragment ->{
                        ivIcon.setImageResource(R.drawable.ic_quiz)
                        tvFragmentName.text= ContextCompat.getString(binding.root.context,R.string.quiz)
                    }
                    R.id.itemLogout ->{
                        ivIcon.setImageResource(R.drawable.logout)
                        tvFragmentName.text= ContextCompat.getString(binding.root.context,R.string.logout2)
                    }
                }
                llFragmentDetail.setOnClickListener {
                    onClick(id)
                }
            }
        }
    }
}