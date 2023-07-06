package org.example.kanban.repository.factory;

import org.example.kanban.repository.KanbanRepository;
import org.example.kanban.repository.inmemory.EpictaskInMemoryRepository;
import org.example.kanban.repository.inmemory.SubtaskInMemoryRepository;
import org.example.kanban.repository.inmemory.TaskInMemoryRepository;

public class InMemoryRepositoryFactory implements RepositoryFactory {
    private static RepositoryFactory instance;

    private InMemoryRepositoryFactory() {
    }

    public static RepositoryFactory getInstance() {
        if (instance == null) {
            instance = new InMemoryRepositoryFactory();
        }
        return instance;
    }

    @Override
    public KanbanRepository getTaskRepository() {
        return new TaskInMemoryRepository();
    }

    @Override
    public KanbanRepository getEpictaskRepository() {
        return new EpictaskInMemoryRepository();
    }

    @Override
    public KanbanRepository getSubtaskRepository() {
        return new SubtaskInMemoryRepository();
    }
}
