package com.xinhui.quizapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xinhui.quizapp.data.model.Score
import com.xinhui.quizapp.databinding.ItemStudentLayoutBinding

class StudentScoreAdapter(
    private var rank: List<Score>,
    private val userId: String
) : RecyclerView.Adapter<StudentScoreAdapter.ItemScoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemScoreViewHolder {
        val binding = ItemStudentLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemScoreViewHolder(binding)
    }

    override fun getItemCount() = rank.size

    override fun onBindViewHolder(holder: ItemScoreViewHolder, position: Int) {
        holder.bind(rank[position])
    }

    fun setNewRank(rank: List<Score>){
        this.rank = rank
        notifyDataSetChanged()
    }

    inner class ItemScoreViewHolder(private val binding: ItemStudentLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(score:Score) {
            binding.run{
                rlScore.visibility = View.VISIBLE
                tvScore.text = score.quizScore.toString()
                tvName.text = score.userName
                if (score.userId == userId) tvIsYou.visibility = View.VISIBLE
            }
        }
    }
}