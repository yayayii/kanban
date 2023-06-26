package org.example.kanban.service;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Task;
import org.example.kanban.repository.KanbanInMemoryRepository;
import org.example.kanban.repository.KanbanRepository;

import java.util.Collection;

@Slf4j
public class KanbanServiceImpl implements KanbanService {
    private final KanbanRepository kanbanRepository = new KanbanInMemoryRepository();


    @Override
    public void createTask(Task task) {
        validateTask(task);
        kanbanRepository.createTask(task);

        log.info("Task was created");
    }

    @Override
    public Task getTaskById(long id) {
        Task task = kanbanRepository.getTaskById(id).orElseThrow(() -> new ValidationException("Wrong task id"));

        log.info("Sending task: {}", task);
        return task;
    }

    @Override
    public Collection<Task> getAllTasks() {
        Collection<Task> tasks = kanbanRepository.getAllTasks();

        log.info("Sending tasks: {}", tasks);
        return tasks;
    }

    @Override
    public void updateTask(Task newTask, long id) {
        validateTask(newTask);
        Task oldTask = kanbanRepository.getTaskById(id).orElseThrow(() -> new ValidationException("Wrong task id"));
        kanbanRepository.updateTask(newTask, oldTask);

        log.info("Task was updated");
    }

    @Override
    public void deleteTaskById(long id) {
        kanbanRepository.getTaskById(id).orElseThrow(() -> new ValidationException("Wrong task id"));
        kanbanRepository.deleteTaskById(id);

        log.info("Task was deleted");
    }

    @Override
    public void deleteAllTasks() {
        kanbanRepository.deleteAllTasks();

        log.info("All tasks were deleted");
    }

    private void validateTask(Task task) {
        if (task.getName() == null || task.getName().isBlank()) {
            throw new ValidationException("Name shouldn't be empty");
        }
        if (task.getDescription() == null || task.getDescription().isBlank()) {
            throw new ValidationException("Description shouldn't be empty");
        }
        if (task.getName().length() > 25) {
            throw new ValidationException("Name maximum length is 25 characters");
        }
        if (task.getDescription().length() > 250) {
            throw new ValidationException("Description maximum length is 250 characters");
        }
    }
}