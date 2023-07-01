package org.example.kanban.service;

import org.example.kanban.model.Task;

import java.util.Set;

public interface TaskService {
    void createTask(Task task);
    Task getTaskById(long id);
    Set<Task> getAllTasks();
    Set<Task> getAllKanbanTasks();
    void updateTask(Task newTask, long id);
    void deleteTaskById(long id);
    void deleteAllKanbanTasks();
}
