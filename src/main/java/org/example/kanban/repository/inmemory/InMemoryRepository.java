package org.example.kanban.repository.inmemory;

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
        Set<Task> allTasks = new TreeSet<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epictasks.values());

        return allTasks;
    }

    public void clearRepository() {
        tasks.clear();
        epictasks.clear();
        subtasks.clear();
    }
}
