package org.example.kanban.repository;

import org.example.kanban.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface KanbanRepository {
    void createTask(Task task);
    Optional<Task> getTaskById(long id);
    Collection<Task> getAllTasks();
    void updateTask(Task newTask, Task oldTask);
    void deleteTaskById(long id);
    void deleteAllTasks();
}
