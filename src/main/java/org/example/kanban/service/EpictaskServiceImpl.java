package org.example.kanban.service;

import lombok.extern.slf4j.Slf4j;
import org.example.kanban.repository.EpictaskRepositoryImpl;

@Slf4j
public class EpictaskServiceImpl extends TaskServiceImpl {
    public EpictaskServiceImpl() {
        super(new EpictaskRepositoryImpl());
    }
}
