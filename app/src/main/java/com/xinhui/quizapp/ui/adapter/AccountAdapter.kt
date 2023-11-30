package com.xinhui.quizapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xinhui.quizapp.R
import com.xinhui.quizapp.data.model.Account
import com.xinhui.quizapp.databinding.ItemGroupScoreQuizSimpleLayoutBinding

class AccountAdapter(
    private var accounts: List<Account>,
    private val click:(Account)->Unit
): RecyclerView.Adapter<AccountAdapter.ItemAccountViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountAdapter.ItemAccountViewHolder {
        val binding = ItemGroupScoreQuizSimpleLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemAccountViewHolder(binding)
    }

    override fun getItemCount() = accounts.size

    override fun onBindViewHolder(holder: ItemAccountViewHolder, position: Int) {
        holder.bind(accounts[position])
    }

    fun setNewAccountLists(accounts: List<Account>) {
        this.accounts = accounts
        notifyDataSetChanged()
    }

    inner class ItemAccountViewHolder(private val binding: ItemGroupScoreQuizSimpleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            binding.run {
                rlAccount.visibility = View.VISIBLE
                tvUserEmail.text = account.info.email
                tvUserName.text = account.info.name
                Glide.with(binding.root)
                    .load(account.profile)
                    .placeholder(R.drawable.ic_person)
                    .into(ivProfilePic)
                ivAdd.visibility = if (account.notValidToAdd) View.GONE else View.VISIBLE
                ivAdd.setOnClickListener { click(account) }
            }
        }
    }
}