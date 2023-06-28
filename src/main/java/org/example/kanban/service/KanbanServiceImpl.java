package org.example.kanban.service;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;
import org.example.kanban.repository.KanbanInMemoryRepository;
import org.example.kanban.repository.KanbanRepository;

import java.util.Set;

@Slf4j
public class KanbanServiceImpl implements KanbanService {
    private final KanbanRepository kanbanRepository;


    public KanbanServiceImpl() {
        kanbanRepository = new KanbanInMemoryRepository();
    }

    public KanbanServiceImpl(KanbanRepository kanbanRepository) {
        this.kanbanRepository = kanbanRepository;
    }


    //All tasks
    @Override
    public Set<Task> getAllTasks() {
        Set<Task> tasks = kanbanRepository.getAllTasks();

        log.info("Sending tasks: {}", tasks);
        return tasks;
    }

    @Override
    public void deleteAllTasks() {
        kanbanRepository.deleteAllTasks();

        log.info("All tasks were deleted");
    }

    //Tasks
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

    //Subtasks
    @Override
    public void createSubtask(Subtask subtask, long epictaskId) {
        validateTask(subtask);
        Epictask epictask = kanbanRepository.getEpictaskById(epictaskId)
                .orElseThrow(() -> new ValidationException("Wrong epictask id"));
        subtask.setEpictask(epictask);
        kanbanRepository.createSubtask(subtask);

        log.info("Subtask was created");
    }

    @Override
    public Subtask getSubtaskById(long id) {
        Subtask subtask = kanbanRepository.getSubtaskById(id)
                .orElseThrow(() -> new ValidationException("Wrong subtask id"));

        log.info("Sending subtask: {}", subtask);
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask newSubtask, long id) {
        validateTask(newSubtask);
        Subtask oldSubtask = kanbanRepository.getSubtaskById(id)
                .orElseThrow(() -> new ValidationException("Wrong subtask id"));
        kanbanRepository.updateSubtask(newSubtask, oldSubtask);

        log.info("Subtask was updated");
    }

    @Override
    public void deleteSubtaskById(long id) {
        kanbanRepository.getSubtaskById(id).orElseThrow(() -> new ValidationException("Wrong subtask id"));
        kanbanRepository.deleteSubtaskById(id);

        log.info("Subtask was deleted");
    }

    //Epictasks
    @Override
    public void createEpictask(Epictask epictask) {
        validateTask(epictask);
        kanbanRepository.createEpictask(epictask);

        log.info("Epictask was created");
    }

    @Override
    public Epictask getEpictaskById(long id) {
        Epictask epictask = kanbanRepository.getEpictaskById(id)
                .orElseThrow(() -> new ValidationException("Wrong epictask id"));

        log.info("Sending epictask: {}", epictask);
        return epictask;
    }

    @Override
    public Set<Epictask> getAllEpictasks() {
        Set<Epictask> epictasks = kanbanRepository.getAllEpictasks();

        log.info("Sending epictasks: {}", epictasks);
        return kanbanRepository.getAllEpictasks();
    }

    @Override
    public void updateEpictask(Epictask newEpictask, long id) {
        validateTask(newEpictask);
        Epictask oldEpictask = kanbanRepository.getEpictaskById(id)
                .orElseThrow(() -> new ValidationException("Wrong epictask id"));
        kanbanRepository.updateEpictask(newEpictask, oldEpictask);

        log.info("Epictask was updated");
    }

    @Override
    public void deleteEpictaskById(long id) {
        kanbanRepository.getEpictaskById(id).orElseThrow(() -> new ValidationException("Wrong epictask id"));
        kanbanRepository.deleteEpictaskById(id);

        log.info("Epictask was deleted");
    }


    private void validateTask(Task task) {
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
    }
}
