package com.xinhui.quizapp.ui.screen.addEditQuiz

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xinhui.quizapp.ui.screen.addEditQuiz.viewModel.AddQuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddQuizFragment : BaseAddEditQuizFragment() {
    override val viewModel: AddQuizViewModel by viewModels()

    override fun setupUIComponents() {
        super.setupUIComponents()
        binding.tvSelectGroup.setOnClickListener {
            studentGrpDialog()
        }
    }
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.success.collect{
                navController.popBackStack()
            }
        }
    }

    private fun studentGrpDialog(){
        initialSelectedGroup = selectedGroup.copyOf()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select student group")
        builder.setCancelable(false)
        val groupNames = groupNameArr
        builder.setMultiChoiceItems(groupNameArr, selectedGroup) { _, i, b ->
            if (b) {
                groupList.add(i)
                groupList.sort()
            } else {
                groupList.remove(i)
            }
        }
        builder.setPositiveButton("OK") { _, _ ->
            val stringBuilder = StringBuilder()
            selectedGroupArr.clear()
            for (j in 0 until groupList.size) {
                stringBuilder.append(groupNameArr[groupList[j]])
                selectedGroupArr.add(studentGrps[groupList[j]].id.toString())
                if (j != groupList.size - 1) stringBuilder.append(", ")
            }
            binding.tvSelectGroup.text = stringBuilder.toString()
        }
        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
            selectedGroup = initialSelectedGroup.copyOf()
            groupList.clear()
            for (i in selectedGroup.indices) {
                if (selectedGroup[i]) {
                    groupList.add(i)
                }
            }
            dialogInterface.dismiss()
        }
        builder.setNeutralButton("Clear All") { _, _ ->
            for (j in selectedGroup.indices) selectedGroup[j] = false
            selectedGroupArr.clear()
            groupList.clear()
            binding.tvSelectGroup.text = ""
        }
        for (i in groupNames.indices) {
            if (selectedGroupArr.contains(studentGrps[i].id.toString())) {
                selectedGroup[i] = true
                groupList.add(i)
            }
        }
        builder.show()
    }
}