package com.example.todolist

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityUpdateTaskBinding
import java.util.Calendar


class UpdateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateTaskBinding
    private lateinit var db: TasksDatabaseHelper
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 创建一个包含数据的列表
        val items = listOf("low", "medium", "high")

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, items)

        binding.updateWeight.adapter = adapter

        binding.updateDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    binding.updateDate.setText(selectedDate)
                }, year, month, day)
            datePickerDialog.show()

        }

        db = TasksDatabaseHelper(this)

        taskId = intent.getIntExtra("task_Id", -1)
        if (taskId == -1) {
            finish()
            return
        }
        val task = db.getTaskById(taskId)
        binding.updateTitleEditText.setText(task.title)
        when(task.weight) {
            "low" -> binding.updateWeight.setSelection(0)
            "medium" -> binding.updateWeight.setSelection(1)
            "high" -> binding.updateWeight.setSelection(2)
        }
        binding.updateDate.setText(task.date)
        binding.updateContentEditText.setText(task.content)



        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newWeight = binding.updateWeight.selectedItem.toString()
            val newDate = binding.updateDate.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val updatedTask = Task(taskId, newTitle, newWeight, newDate, newContent)
            db.updateTask(updatedTask)
            finish()
            Toast.makeText(this, "Change saved", Toast.LENGTH_SHORT).show()
        }
    }
}