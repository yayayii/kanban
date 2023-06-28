package org.example.kanban.repository;

import org.example.kanban.model.Subtask;

import java.util.Optional;

public interface SubtaskRepository {
    void createSubtask(Subtask subtask);
    Optional<Subtask> getSubtaskById(long id);
    void updateSubtask(Subtask newSubtask, Subtask oldSubtask);
    void deleteSubtaskById(long id);
}
