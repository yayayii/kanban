package org.example.kanban.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class KanbanInMemoryRepository implements KanbanRepository {
    private final Map<Long, Task> tasks = new HashMap<>();
    private static long id = 1;


    @Override
    public void createTask(Task task) {
        task.setId(id);
        task.setStatus(TaskStatus.NEW.getStatusName());
        tasks.put(id, task);
        id++;
    }

    @Override
    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    @Override
    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    @Override
    public void updateTask(Task newTask, long id) {
        Task oldTask = tasks.get(id);
        if (newTask.getName() != null && !newTask.getName().isBlank()) {
            oldTask.setName(newTask.getName());
        }
        if (newTask.getDescription() != null && !newTask.getDescription().isBlank()) {
            oldTask.setDescription(newTask.getDescription());
        }
        if (newTask.getStatus() != null) {
            oldTask.setStatus(newTask.getStatus());
        }
    }

    @Override
    public void deleteTaskById(long id) {
        tasks.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }


    private boolean containsTask(long id) {
        return tasks.containsKey(id);
    }
}
