package com.xinhui.quizapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xinhui.quizapp.data.model.StudentGroup
import com.xinhui.quizapp.databinding.ItemGroupScoreQuizSimpleLayoutBinding

class GroupAdapter(
    private var groups: List<StudentGroup>,
    private val onClick:(String)->Unit
): RecyclerView.Adapter<GroupAdapter.ItemQuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemQuizViewHolder {
        val binding = ItemGroupScoreQuizSimpleLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemQuizViewHolder(binding)
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: ItemQuizViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    fun setNewGroups(groups: List<StudentGroup>){
        this.groups = groups
        notifyDataSetChanged()
    }

    inner class ItemQuizViewHolder(private val binding: ItemGroupScoreQuizSimpleLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(group:StudentGroup){
            binding.run{
                tvGroupName.text = group.name
                llGroup.setOnClickListener { onClick(group.id!!) }
                llGroup.visibility = View.VISIBLE
            }
        }
    }
}