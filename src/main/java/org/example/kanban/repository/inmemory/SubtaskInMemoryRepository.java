package org.example.kanban.repository.inmemory;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SubtaskInMemoryRepository extends InMemoryRepository {
    @Override
    public Task createTask(Task subtask) {
        subtask.setId(id);
        subtask.setStatus(TaskStatus.NEW.getStatusName());
        subtasks.put(id, (Subtask) subtask);
        epictasks.get(((Subtask) subtask).getEpictaskId()).getSubtasks().add((Subtask) subtask);
        id++;

        return subtask;
    }

    @Override
    public Optional<Task> getTaskById(long id) {
        return Optional.ofNullable(subtasks.get(id));
    }

    @Override
    public Set<Task> getAllTasks() {
        return new HashSet<>(subtasks.values());
    }

    @Override
    public void updateTask(Task newSubtask, Task oldSubtask) {
        oldSubtask.setName(newSubtask.getName());
        oldSubtask.setDescription(newSubtask.getDescription());
        oldSubtask.setStatus(newSubtask.getStatus());
        updateEpictaskStatus(((Subtask) oldSubtask).getEpictaskId());
    }

    @Override
    public void deleteTaskById(long id) {
        Subtask subtask = subtasks.get(id);
        Epictask epictask = epictasks.get(subtask.getEpictaskId());
        epictask.getSubtasks().remove(subtask);
        subtasks.remove(id);
        updateEpictaskStatus(epictask.getId());
    }
}
