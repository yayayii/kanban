package org.example.kanban.service;

import org.example.kanban.model.Task;

import java.util.Set;

public interface KanbanService extends TaskService, SubtaskService, EpictaskService {
    Set<Task> getAllTasks();
    void deleteAllTasks();
}
