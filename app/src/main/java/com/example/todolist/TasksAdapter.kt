package com.example.todolist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView



class TasksAdapter(private var tasks: List<Task>, context: Context): RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private val db = TasksDatabaseHelper(context)

    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val weightTextView: TextView = itemView.findViewById(R.id.weightTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.contentTextView.text = task.content
        holder.dateTextView.text = task.date
        holder.weightTextView.text = task.weight

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateTaskActivity::class.java).apply {
                putExtra("task_Id", task.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener {
            db.deleteTask(task.id)
            refreshData(db.getAllTasks())
            Toast.makeText(holder.itemView.context, "Task deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun refreshData(newTasks: List<Task>) {
        val priorityOrder = listOf("high", "medium", "low")
        tasks = newTasks.sortedBy { priorityOrder.indexOf(it.weight) }
        notifyDataSetChanged()
    }
}