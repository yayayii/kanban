package org.example.kanban.repository;

import org.example.kanban.model.Task;

import java.util.Optional;

public interface TaskRepository {
    void createTask(Task task);
    Optional<Task> getTaskById(long id);
    void updateTask(Task newTask, Task oldTask);
    void deleteTaskById(long id);
}
