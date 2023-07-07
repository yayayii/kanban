package org.example.kanban.repository.implementation;

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
        Epictask epictask = epictasks.get(((Subtask) subtask).getEpictaskId());
        epictask.getSubtasks().add((Subtask) subtask);
        updateEpictaskEndtime(epictask.getId());
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
        Epictask epictask = epictasks.get(((Subtask) oldSubtask).getEpictaskId());
        updateEpictaskStatus(epictask.getId());
        updateEpictaskEndtime(epictask.getId());
    }

    @Override
    public void deleteTaskById(long id) {
        Subtask subtask = subtasks.get(id);
        Epictask epictask = epictasks.get(subtask.getEpictaskId());
        epictask.getSubtasks().remove(subtask);
        subtasks.remove(id);
        updateEpictaskStatus(epictask.getId());
        updateEpictaskEndtime(epictask.getId());
    }


    private void updateEpictaskStatus(long epictaskId) {
        Epictask epictask = epictasks.get(epictaskId);
        boolean isNew = true;
        boolean isDone = true;

        Set<Subtask> epicSubtasks = epictask.getSubtasks();
        for (Subtask subtask: epicSubtasks) {
            if (!subtask.getStatus().equals(TaskStatus.NEW.getStatusName())) {
                isNew = false;
                break;
            }
        }
        for (Subtask subtask: epicSubtasks) {
            if (!subtask.getStatus().equals(TaskStatus.DONE.getStatusName())) {
                isDone = false;
                break;
            }
        }

        if (isNew) {
            epictask.setStatus(TaskStatus.NEW.getStatusName());
        } else if (isDone) {
            epictask.setStatus(TaskStatus.DONE.getStatusName());
        } else {
            epictask.setStatus(TaskStatus.IN_PROGRESS.getStatusName());
        }
    }

    private void updateEpictaskEndtime(long epictaskId) {
        Epictask epictask = epictasks.get(epictaskId);
        Optional<Subtask> epictaskEndtime = epictask.getSubtasks().stream().max((o1, o2) -> {
            if (o1.getEndTime() == null && o2.getEndTime() == null) {
                return 0;
            }
            if (o1.getEndTime() == null) {
                return -1;
            }
            if (o2.getEndTime() == null) {
                return 1;
            }
            return o1.getEndTime().compareTo(o2.getEndTime());
        });
        if (epictaskEndtime.isPresent()) {
            epictask.setEndTime(epictaskEndtime.get().getEndTime());
        } else {
            epictask.setEndTime(null);
        }
        epictaskEndtime.ifPresent(subtask -> epictask.setEndTime(subtask.getEndTime()));
    }
}
