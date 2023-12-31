package org.example.kanban.service;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Task;
import org.example.kanban.repository.KanbanRepository;
import org.example.kanban.repository.factory.RepositoryFactory;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
public class TaskService implements KanbanService {
    protected final KanbanRepository kanbanRepository;


    public TaskService(RepositoryFactory factory) {
        kanbanRepository = factory.getTaskRepository();
    }

    public TaskService(KanbanRepository kanbanRepository) {
        this.kanbanRepository = kanbanRepository;
    }


    @Override
    public Task createTask(Task task) {
        validateTask(task);
        kanbanRepository.createTask(task);

        log.info("Task {} was created", task);
        return task;
    }

    @Override
    public Task getTaskById(long id) {
        Task task = kanbanRepository.getTaskById(id).orElseThrow(() -> new ValidationException("Wrong task id"));

        log.info("Sending task: {}", task);
        return task;
    }

    @Override
    public Set<Task> getAllTasks() {
        Set<Task> tasks = kanbanRepository.getAllTasks();

        log.info("Sending all tasks: {}", tasks);
        return tasks;
    }

    @Override
    public Set<Task> getAllKanbanTasks() {
        Set<Task> kanbanTasks = kanbanRepository.getRepository();

        log.info("Sending all kanban tasks: {}", kanbanTasks);
        return kanbanTasks;
    }

    @Override
    public Task updateTask(Task newTask, long id) {
        validateTask(newTask);
        Task oldTask = kanbanRepository.getTaskById(id).orElseThrow(() -> new ValidationException("Wrong task id"));
        kanbanRepository.updateTask(newTask, oldTask);

        log.info("Task {} was updated", oldTask);
        return oldTask;
    }

    @Override
    public void deleteTaskById(long id) {
        kanbanRepository.getTaskById(id).orElseThrow(() -> new ValidationException("Wrong task id"));
        kanbanRepository.deleteTaskById(id);

        log.info("Task was deleted");
    }

    @Override
    public void deleteAllKanbanTasks() {
        kanbanRepository.clearRepository();

        log.info("All kanban tasks were deleted");
    }


    protected void validateTask(Task task) {
        if (task.getName() == null || task.getName().isBlank()) {
            throw new ValidationException("Name shouldn't be empty");
        }
        if (task.getName().length() > 25) {
            throw new ValidationException("Name maximum length is 25 characters");
        }
        if (task.getDescription() == null || task.getDescription().isBlank()) {
            throw new ValidationException("Description shouldn't be empty");
        }
        if (task.getDescription().length() > 250) {
            throw new ValidationException("Description maximum length is 250 characters");
        }
        if (task.getStatus() != null && !TaskStatus.isValid(task.getStatus())) {
            throw new ValidationException("Wrong task status");
        }
        if (task.getEndTime() != null && task.getEndTime().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Task end time should be in the future");
        }
    }
}
