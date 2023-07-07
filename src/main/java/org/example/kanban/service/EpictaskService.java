package org.example.kanban.service;

import org.example.kanban.repository.factory.RepositoryFactory;

public class EpictaskService extends TaskService {
    public EpictaskService(RepositoryFactory factory) {
        super(factory.getEpictaskRepository());
    }
}
