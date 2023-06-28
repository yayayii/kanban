package org.example.kanban.service;

import org.example.kanban.model.Epictask;

import java.util.Set;

public interface EpictaskService {
    void createEpictask(Epictask epictask);
    Epictask getEpictaskById(long id);
    Set<Epictask> getAllEpictasks();
    void updateEpictask(Epictask newEpictask, long id);
    void deleteEpictaskById(long id);
}
