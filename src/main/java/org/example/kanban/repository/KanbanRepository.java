package org.example.kanban.repository;

import org.example.kanban.model.Task;

import java.util.Collection;

public interface KanbanRepository {
    void createTask(Task task);
    Task getTaskById(long id);
    Collection<Task> getAllTasks();
    void updateTask(Task newTask, long id);
    void deleteTaskById(long id);
    void deleteAllTasks();
}
