package org.example.kanban.service;

import org.example.kanban.model.Task;

import java.util.Collection;

public interface KanbanService {
    void createTask(Task task);
    Task getTaskById(long id);
    Collection<Task> getAllTasks();
    void updateTask(Task newTask, long id);
    void deleteTaskById(long id);
    void deleteAllTasks();
}
