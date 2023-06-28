package org.example.kanban.repository;

import org.example.kanban.model.Epictask;
import org.example.kanban.model.Task;

import java.util.Optional;

public interface EpictaskRepository {
    void createEpictask(Epictask epictask);
    Optional<Epictask> getEpictaskById(long id);
    void updateEpictask(Epictask newEpictask, Epictask oldEpictask);
    void deleteEpictaskById(long id);
}
