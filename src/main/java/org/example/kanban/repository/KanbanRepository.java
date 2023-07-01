package org.example.kanban.repository;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;

import java.util.*;

public abstract class KanbanRepository {
    protected static final Map<Long, Task> tasks = new HashMap<>();
    protected static final Map<Long, Epictask> epictasks = new HashMap<>();
    protected static final Map<Long, Subtask> subtasks = new HashMap<>();
    protected long id = 1;


    public abstract void createTask(Task task);
    public abstract Optional<Task> getTaskById(long id);
    public abstract Set<Task> getAllTasks();
    public abstract void updateTask(Task newTask, Task oldTask);
    public abstract void deleteTaskById(long id);


    public Set<Task> getAllKanbanTasks() {
        Set<Task> allTasks = new HashSet<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epictasks.values());
        allTasks.addAll(subtasks.values());

        return allTasks;
    }

    public void deleteAllKanbanTasks() {
        tasks.clear();
        epictasks.clear();
        subtasks.clear();
    }


    protected void updateEpictaskStatus(long epictaskId) {
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
