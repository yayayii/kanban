package org.example.kanban.repository.factory;

import org.example.kanban.repository.KanbanRepository;
import org.example.kanban.repository.implementation.EpictaskInMemoryRepository;
import org.example.kanban.repository.implementation.SubtaskInMemoryRepository;
import org.example.kanban.repository.implementation.TaskInMemoryRepository;

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
