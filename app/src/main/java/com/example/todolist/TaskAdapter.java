package com.example.todolist;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todolist.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<Task> taskList;
    private OnTaskCheckedChangeListener listener; // Listener for checkbox changes

    public TaskAdapter(Context context, List<Task> taskList, OnTaskCheckedChangeListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener; // Initialize the listener
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.todoitem, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());
        holder.taskPriority.setText(task.getPriority());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(task.getDueDate());
        holder.taskDueDate.setText(formattedDate);

        // Set the checkbox state based on the 'mark_completed' attribute
        holder.checkbox.setChecked(task.getMarkCompleted().equals("completed"));

        // Handle checkbox state changes
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the 'mark_completed' attribute based on checkbox state
                String markCompleted = isChecked ? "completed" : "not_completed";
                task.setMarkCompleted(markCompleted);

                // Notify the listener that the checkbox state has changed
                if (listener != null) {
                    listener.onTaskCheckedChanged(task);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription, taskDueDate, taskPriority;
        CheckBox checkbox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskDueDate = itemView.findViewById(R.id.taskDueDate);
            taskPriority = itemView.findViewById(R.id.taskPriority);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

    // Interface for checkbox change listener
    public interface OnTaskCheckedChangeListener {
        void onTaskCheckedChanged(Task task);
    }
}


