package org.example.kanban.service;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;
import org.example.kanban.repository.EpictaskRepositoryImpl;
import org.example.kanban.repository.KanbanRepository;
import org.example.kanban.repository.SubtaskRepositoryImpl;

@Slf4j
public class SubtaskServiceImpl extends TaskServiceImpl {
    private final KanbanRepository epictaskRepository;


    public SubtaskServiceImpl() {
        super(new SubtaskRepositoryImpl());
        epictaskRepository = new EpictaskRepositoryImpl();
    }

    public SubtaskServiceImpl(KanbanRepository kanbanRepository, KanbanRepository epictaskRepository) {
        super(kanbanRepository);
        this.epictaskRepository = epictaskRepository;
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
