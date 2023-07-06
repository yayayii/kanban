package org.example.kanban.repository.inmemory;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EpictaskInMemoryRepository extends InMemoryRepository {
    @Override
    public Task createTask(Task epictask) {
        epictask.setId(id);
        epictask.setStatus(TaskStatus.NEW.getStatusName());
        epictasks.put(id, (Epictask) epictask);
        id++;

        return epictask;
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
        Set<Subtask> epicSubtasks = epictask.getSubtasks();
        for (Subtask subtask: epicSubtasks) {
            subtasks.remove(subtask.getId());
        }
        epictasks.remove(id);
    }
}
