package org.example.kanban.repository.inmemory;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;
import org.example.kanban.repository.KanbanRepository;

import java.util.*;

public abstract class InMemoryRepository implements KanbanRepository {
    protected static final Map<Long, Task> tasks = new HashMap<>();
    protected static final Map<Long, Epictask> epictasks = new HashMap<>();
    protected static final Map<Long, Subtask> subtasks = new HashMap<>();
    protected static long id = 1;


    public Set<Task> getRepository() {
        Set<Task> allTasks = new HashSet<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epictasks.values());

        return allTasks;
    }

    public void clearRepository() {
        tasks.clear();
        epictasks.clear();
        subtasks.clear();
    }


    protected void updateEpictaskStatus(long epictaskId) {
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
}
