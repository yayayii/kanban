package org.example.kanban.repository;

import org.example.kanban.model.Task;

import java.util.Set;

public interface KanbanRepository extends TaskRepository, EpictaskRepository, SubtaskRepository {
    Set<Task> getAllTasks();
    void deleteAllTasks();
}
