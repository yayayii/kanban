package org.example.kanban.repository;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Task;

import java.util.*;

public class TaskRepositoryImpl extends KanbanRepository {
    @Override
    public void createTask(Task task) {
        task.setId(id);
        task.setStatus(TaskStatus.NEW.getStatusName());
        tasks.put(id, task);
        id++;
    }

    @Override
    public Optional<Task> getTaskById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public Set<Task> getAllTasks() {
        return new HashSet<>(tasks.values());
    }

    @Override
    public void updateTask(Task newTask, Task oldTask) {
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setStatus(newTask.getStatus());
    }

    @Override
    public void deleteTaskById(long id) {
        tasks.remove(id);
    }

    @Override
    public void deleteAllKanbanTasks() {
        tasks.clear();
        epictasks.clear();
        subtasks.clear();
    }
}
