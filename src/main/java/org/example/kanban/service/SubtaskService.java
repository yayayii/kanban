package org.example.kanban.service;

import org.example.kanban.model.Subtask;

public interface SubtaskService {
    void createSubtask(Subtask subtask, long epictaskId);
    Subtask getSubtaskById(long id);
    void updateSubtask(Subtask newSubtask, long id);
    void deleteSubtaskById(long id);
}
