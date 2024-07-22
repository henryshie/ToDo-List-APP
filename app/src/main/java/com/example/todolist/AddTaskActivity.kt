package com.example.todolist

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityAddTaskBinding


import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TasksDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        // 创建一个包含数据的列表
        val items = listOf("low", "medium", "high")

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, items)

        binding.weight.adapter = adapter

        binding.date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    binding.date.setText(selectedDate)
                }, year, month, day)
            datePickerDialog.show()

        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val weight = binding.weight.selectedItem.toString()
            val date = binding.date.text.toString()
            val task = Task(0, title, weight, date, content)
            db.insertTask(task)
            finish()
            Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show()
        }
    }
}