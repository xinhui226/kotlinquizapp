package com.xinhui.quizapp.core.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar

class DateTimePicker(val context: Context) {
    var selectedDate: String = ""

    fun showDatePicker(textView: TextView,string: (String)->Unit) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
        var date: Calendar = Calendar.getInstance()
            date.set(year, monthOfYear, dayOfMonth)
            selectedDate = SimpleDateFormat("yyyy-MM-dd").format(date.time)
            textView.text = selectedDate
            string(selectedDate)
        }, year, month, day)

        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }
}
