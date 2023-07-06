package org.example.kanban.service;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;
import org.example.kanban.repository.factory.RepositoryFactory;
import org.example.kanban.repository.KanbanRepository;

@Slf4j
public class SubtaskService extends TaskService {
    private final KanbanRepository epictaskRepository;


    public SubtaskService(RepositoryFactory factory) {
        super(factory.getSubtaskRepository());
        epictaskRepository = factory.getEpictaskRepository();
    }
    //test constructor
    public SubtaskService(KanbanRepository kanbanRepository) {
        super(kanbanRepository);
        this.epictaskRepository = kanbanRepository;
    }

    @Override
    public Task createTask(Task subtask) {
        validateTask(subtask);
        epictaskRepository.getTaskById(((Subtask) subtask).getEpictaskId())
                .orElseThrow(() -> new ValidationException("Wrong epictask id"));
        kanbanRepository.createTask(subtask);

        log.info("Task {} was created", subtask);
        return subtask;
    }
}
