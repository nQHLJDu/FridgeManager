package com.example.FridgeManager

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = arguments?.getInt("year", calendar.get(Calendar.YEAR)) ?: calendar.get(Calendar.YEAR)
        val month = arguments?.getInt("month", calendar.get(Calendar.MONTH)) ?: calendar.get(Calendar.MONTH)
        val day = arguments?.getInt("day", calendar.get(Calendar.DAY_OF_MONTH)) ?: calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Light_Dialog,
            this,
            year,
            month,
            day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener?.onDateSet(view, year, month, dayOfMonth)
    }
}
