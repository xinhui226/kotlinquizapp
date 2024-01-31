package com.xinhui.quizapp.ui.screen.addEditQuiz

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhui.quizapp.R
import com.xinhui.quizapp.core.utils.DateTimePicker
import com.xinhui.quizapp.data.model.Question
import com.xinhui.quizapp.data.model.Quiz
import com.xinhui.quizapp.data.model.StudentGroup
import com.xinhui.quizapp.databinding.FragmentAddEditQuizBinding
import com.xinhui.quizapp.ui.adapter.QuizQuestionsAdapter
import com.xinhui.quizapp.ui.screen.addEditQuiz.viewModel.BaseAddEditQuizViewModel
import com.xinhui.quizapp.ui.screen.base.BaseFragment
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader


abstract class BaseAddEditQuizFragment:BaseFragment<FragmentAddEditQuizBinding>() {
    override val viewModel: BaseAddEditQuizViewModel by viewModels()

    var questionsAdapter = QuizQuestionsAdapter(emptyList())
    var groupNameArr = arrayOf<String>()
    val groupList = ArrayList<Int>()
    var initialSelectedGroup = BooleanArray(0)
    var selectedGroup = BooleanArray(groupNameArr.size)
    var selectedGroupArr = mutableListOf<String>()
    var studentGrps = emptyList<StudentGroup>()
    var selectedDate = ""
    var isPublished = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents() {
        super.setupUIComponents()
        binding.run {
            btnSetDateTime.setOnClickListener{
                DateTimePicker(requireContext()).showDatePicker(tvQuizDate){
                    selectedDate = it
                }
            }
            btnUpload.setOnClickListener {
                getContent.launch("text/*")
            }
            ivQuestionMark.setOnClickListener { helpDialog() }
            btnAdd.setOnClickListener {
                val titles = mutableListOf<String>()
                val options = mutableListOf<String>()
                val answers = mutableListOf<String>()
                val seconds = mutableListOf<Long>()
                questionsAdapter.getLatestQuestions().map {
                    titles.add((it.titles))
                    it.options.map { option -> options.add(option) }
                    answers.add(it.answers)
                    seconds.add(it.seconds)
                }
                viewModel.submit(
                    Quiz(
                    name = etQuizName.text.toString(),
                    date = selectedDate,
                    titles = titles,
                    options = options,
                    answers = answers,
                    seconds = seconds,
                    groups = selectedGroupArr,
                        isPublished = isPublished)
                )
            }
            spinnerStatus.apply {
                adapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_spinner_item, listOf(true,false))
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                         isPublished = parent.getItemAtPosition(pos).toString().toBoolean()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.groups.collect{
                studentGrps = it
                val newGroupArr = groupNameArr.toMutableList()
                it.forEach { grp->
                    newGroupArr.add(grp.name)
                }
                groupNameArr = newGroupArr.toTypedArray()
                selectedGroup = BooleanArray(groupNameArr.size)
            }
        }
        lifecycleScope.launch {
            viewModel.questions.collect{
                if (it.isNotEmpty()) setupQuestionsAdapter(it)
            }
        }
    }

    protected fun setupQuestionsAdapter(questions: List<Question>) {
        questionsAdapter = QuizQuestionsAdapter(questions)
        binding.rvQuestions.adapter = questionsAdapter
        binding.rvQuestions.layoutManager = LinearLayoutManager(requireContext())
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent())
    { uri: Uri? ->
        uri?.let {
            val documentFile = DocumentFile.fromSingleUri(requireContext(), it)
            if (documentFile != null && documentFile.isFile) {
                val fileExtension = documentFile.name?.substringAfterLast(".", "")
                if (fileExtension.equals("csv", ignoreCase = true)) {
                    binding.tvFilename.visibility = View.VISIBLE
                    binding.tvFilename.text = documentFile.name.toString()
                    val csvFile = requireActivity().contentResolver.openInputStream(it)
                    val isr = InputStreamReader(csvFile)
                    BufferedReader(isr).readLines().let { lines ->
                        viewModel.readCSV(lines.subList(1, lines.size))
                    }
                }
                else {
                    showSnackbar("Invalid file uploaded, only CSV file acceptable", true)
                }
            }
        }
    }

    private fun helpDialog(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        builder.setTitle("Example format for CSV file")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog, null)
        val ivSample  = dialogLayout.findViewById<HorizontalScrollView>(R.id.svCSVSample)
        ivSample.visibility = View.VISIBLE
        val tvNotice  = dialogLayout.findViewById<TextView>(R.id.tvNotice)
        tvNotice.visibility = View.VISIBLE
        builder.setView(dialogLayout)
        builder.setPositiveButton("CLOSE") { di, _ ->
            di.dismiss()
        }
        builder.show()
    }
}