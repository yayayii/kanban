package org.example.kanban.service;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.repository.factory.RepositoryFactory;

@Slf4j
public class EpictaskService extends TaskService {
    public EpictaskService(RepositoryFactory factory) {
        super(factory.getEpictaskRepository());
    }
}
