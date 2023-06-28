package org.example.kanban.service;

import org.example.kanban.model.Task;

public interface TaskService {
    void createTask(Task task);
    Task getTaskById(long id);
    void updateTask(Task newTask, long id);
    void deleteTaskById(long id);
}
