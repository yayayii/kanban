package org.example.kanban.repository;

import org.example.kanban.model.Task;

import java.util.Optional;
import java.util.Set;

public interface KanbanRepository {
    Task createTask(Task task);
    Optional<Task> getTaskById(long id);
    Set<Task> getAllTasks();
    Set<Task> getRepository();
    void updateTask(Task newTask, Task oldTask);
    void deleteTaskById(long id);
    void clearRepository();
}
