package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    public static final String KEY_EXTRA_TASK_ID = "taskListFragment.task_id";
    public static final String KEY_SAVED_SUBTITLE = "saved_subtitle";
    private TaskAdapter adapter;
    private String savedSubtitle;
    private boolean subtitleVisible=false;
    public RecyclerView recyclerView;

    private void updateView(){
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();

        if (adapter == null) {
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        }
        else
            adapter.notifyDataSetChanged();
        updateSubtitle();
    }

    public void updateSubtitle(){
        TaskStorage taskStorage=TaskStorage.getInstance();
        List<Task> tasks=taskStorage.getTasks();
        int todoTasksCount = 0;
        for (Task task : tasks){
            if(!task.isDone()){
                todoTasksCount++;
            }
        }
        String subtitle = getString(R.string.subtitle_format) + todoTasksCount;
        savedSubtitle=subtitle;
        if (!subtitleVisible){
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SAVED_SUBTITLE,subtitleVisible);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()) {
           case R.id.new_task:
               Task task = new Task();
               TaskStorage.getInstance().addTask(task);
               Intent intent = new Intent(getActivity(), MainActivity.class);
               intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
               startActivity(intent);
               return true;
           case R.id.show_subtitle:
               subtitleVisible=!subtitleVisible;
               getActivity().invalidateOptionsMenu();
               updateSubtitle();
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);

        inflater.inflate(R.menu.fragment_task_menu,menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);

        if (!subtitleVisible){
            subtitleItem.setTitle(R.string.show_subtitle);
        }else{
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        if(savedInstanceState!=null)
        subtitleVisible=savedInstanceState.getBoolean(KEY_SAVED_SUBTITLE);

        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView nameTextView;
        private TextView dateTextView;
        private ImageView iconImageView;
        private Task task;

        public TaskHolder(@NonNull LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
        }

        public void bind(Task task){
            this.task = task;

            iconImageView = itemView.findViewById(R.id.task_checkmark);

            if (this.task.isDone()){
                iconImageView.setImageResource(R.drawable.task_done_image);
            }else{
                iconImageView.setImageResource(R.drawable.ic_check_box_outline_blank);
            }


            nameTextView.setText(task.getName());
            dateTextView.setText(task.getDate().toString());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID,task.getId());
            startActivity(intent);
        }


    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task=tasks.get(position);
            holder.bind(task);
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater,parent);
        }



        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
}
