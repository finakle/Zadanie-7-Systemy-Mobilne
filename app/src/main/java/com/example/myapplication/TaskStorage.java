package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();
    private List<Task> tasks=new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }

    private TaskStorage() {
        for(int i=1;i<101;i++)
        {
            Task task=new Task();
            task.setName("Zadanie #" + i);
            task.setDone(i%3==0);
            tasks.add(task);
        }
    }

    public static TaskStorage getInstance() {
        return taskStorage;
    }

    public Task getTask(UUID id){
        for (Task task:tasks){
            if(task.getId().equals(id)) return task;
        }

        return null;
    }

    public void addTask(Task task){
        tasks.add(task);
    }
}
