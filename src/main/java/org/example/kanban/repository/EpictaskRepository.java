package org.example.kanban.repository;

import org.example.kanban.model.Epictask;

import java.util.Optional;
import java.util.Set;

public interface EpictaskRepository {
    void createEpictask(Epictask epictask);
    Optional<Epictask> getEpictaskById(long id);
    Set<Epictask> getAllEpictasks();
    void updateEpictask(Epictask newEpictask, Epictask oldEpictask);
    void deleteEpictaskById(long id);
}
