package org.example.kanban.repository;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EpictaskRepositoryImpl extends KanbanRepository {
    @Override
    public void createTask(Task epictask) {
        epictask.setId(id);
        epictask.setStatus(TaskStatus.NEW.getStatusName());
        epictasks.put(id, (Epictask) epictask);
        id++;
    }

    @Override
    public Optional<Task> getTaskById(long id) {
        return Optional.ofNullable(epictasks.get(id));
    }

    @Override
    public Set<Task> getAllTasks() {
        return new HashSet<>(epictasks.values());
    }

    @Override
    public void updateTask(Task newEpictask, Task oldEpictask) {
        oldEpictask.setName(newEpictask.getName());
        oldEpictask.setDescription(newEpictask.getDescription());
    }

    @Override
    public void deleteTaskById(long id) {
        Epictask epictask = epictasks.get(id);
        for (Subtask subtask: epictask.getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epictasks.remove(id);
    }
}
