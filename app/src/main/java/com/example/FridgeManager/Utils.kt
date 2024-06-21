package com.example.FridgeManager

import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import java.text.SimpleDateFormat
import java.util.*

// 日付選択フラグメントを表示する拡張関数
fun EditText.showDatePickerFragment(fragmentManager: FragmentManager, dateFormat: SimpleDateFormat) {
    val calendar = Calendar.getInstance()
    val dateText = this.text.toString()

    try {
        // 入力されている日付が有効かどうかを確認
        if (dateText.isNotEmpty()) {
            val date = dateFormat.parse(dateText)
            date?.let {
                calendar.time = it
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    val newFragment = DatePickerFragment().apply {
        setListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            this@showDatePickerFragment.setText(dateFormat.format(calendar.time))
        }

        //既に入力されている日付がある場合、それを設定する。
        arguments = Bundle().apply {
            putInt("year", calendar.get(Calendar.YEAR))
            putInt("month", calendar.get(Calendar.MONTH))
            putInt("day", calendar.get(Calendar.DAY_OF_MONTH))
        }
    }

    newFragment.show(fragmentManager, "datePicker")

}
