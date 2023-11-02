package com.example.todolist;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;


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

        holder.editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the editTask click event here
                ((MainActivity) context).openUpdateTaskActivity(task);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.updateTask){
                            Task task = taskList.get(position);
                            ((MainActivity) context).openUpdateTaskActivity(task);
                            return true;
                        } else if (item.getItemId() == R.id.deleteTask) {
                            Task task = taskList.get(position);
                            ((MainActivity) context).deleteTask(task);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
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
        ImageButton editTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskDueDate = itemView.findViewById(R.id.taskDueDate);
            taskPriority = itemView.findViewById(R.id.taskPriority);
            checkbox = itemView.findViewById(R.id.checkbox);
            editTask = itemView.findViewById(R.id.editTask);

        }
    }

    // Interface for checkbox change listener
    public interface OnTaskCheckedChangeListener {
        void onTaskCheckedChanged(Task task);
    }
}


