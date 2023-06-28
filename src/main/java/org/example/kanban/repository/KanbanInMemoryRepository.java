package org.example.kanban.repository;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;

import java.util.*;

public class KanbanInMemoryRepository implements KanbanRepository {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, Epictask> epictasks = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();
    private long id = 1;


    //KanbanRepository
    @Override
    public Set<Task> getAllTasks() {
        Set<Task> allTasks = new HashSet<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epictasks.values());
        allTasks.addAll(subtasks.values());

        return allTasks;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epictasks.clear();
        subtasks.clear();
    }

    //TaskRepository
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
    public void updateTask(Task newTask, Task oldTask) {
        oldTask.setName(newTask.getName());
        oldTask.setDescription(newTask.getDescription());
        oldTask.setStatus(newTask.getStatus());
    }

    @Override
    public void deleteTaskById(long id) {
        tasks.remove(id);
    }

    //SubtaskRepository
    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(id);
        subtask.setStatus(TaskStatus.NEW.getStatusName());
        subtasks.put(id, subtask);
        epictasks.get(subtask.getEpictask().getId()).getSubtasks().add(subtask);
        id++;
    }

    @Override
    public Optional<Subtask> getSubtaskById(long id) {
        return Optional.ofNullable(subtasks.get(id));
    }

    @Override
    public void updateSubtask(Subtask newSubtask, Subtask oldSubtask) {
        oldSubtask.setName(newSubtask.getName());
        oldSubtask.setDescription(newSubtask.getDescription());
        oldSubtask.setStatus(newSubtask.getStatus());
        updateEpictaskStatus(oldSubtask.getEpictask().getId());
    }

    @Override
    public void deleteSubtaskById(long id) {
        Subtask subtask = subtasks.get(id);
        Epictask epictask = epictasks.get(subtask.getEpictask().getId());
        epictask.getSubtasks().remove(subtask);
        subtasks.remove(id);
        updateEpictaskStatus(epictask.getId());
    }

    //EpictaskRepository
    @Override
    public void createEpictask(Epictask epictask) {
        epictask.setId(id);
        epictask.setStatus(TaskStatus.NEW.getStatusName());
        epictasks.put(id, epictask);
        id++;
    }

    @Override
    public Optional<Epictask> getEpictaskById(long id) {
        return Optional.ofNullable(epictasks.get(id));
    }

    @Override
    public Set<Epictask> getAllEpictasks() {
        return new HashSet<>(epictasks.values());
    }

    @Override
    public void updateEpictask(Epictask newEpictask, Epictask oldEpictask) {
        oldEpictask.setName(newEpictask.getName());
        oldEpictask.setDescription(newEpictask.getDescription());
    }

    @Override
    public void deleteEpictaskById(long id) {
        Epictask epictask = epictasks.get(id);
        for (Subtask subtask: epictask.getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epictasks.remove(id);
    }


    private void updateEpictaskStatus(long epictaskId) {
        Epictask epictask = epictasks.get(epictaskId);
        boolean isNew = true;
        boolean isDone = true;

        for (Subtask subtask: epictask.getSubtasks()) {
            if (!subtask.getStatus().equals(TaskStatus.NEW.getStatusName())) {
                isNew = false;
                break;
            }
        }
        for (Subtask subtask: epictask.getSubtasks()) {
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
}
