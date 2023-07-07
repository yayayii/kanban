package org.example.kanban.repository.factory;

import org.example.kanban.repository.KanbanRepository;

public interface RepositoryFactory {
    KanbanRepository getTaskRepository();
    KanbanRepository getEpictaskRepository();
    KanbanRepository getSubtaskRepository();
}
