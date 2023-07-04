package org.example.kanban.repository;

import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class KanbanRepositoryTest {
    private final KanbanRepository taskRepository = new TaskRepositoryImpl();
    private final KanbanRepository subtaskRepository = new SubtaskRepositoryImpl();
    private final KanbanRepository epictaskRepository = new EpictaskRepositoryImpl();


    @Test
    void getAllKanbanTasks_shouldReturnAllCreatedTasks() {
        Task task = mock(Task.class);
        taskRepository.createTask(task);
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        assertTrue(taskRepository.getAllKanbanTasks().contains(task));
        assertTrue(taskRepository.getAllKanbanTasks().contains(epictask));
        assertTrue(taskRepository.getAllKanbanTasks().contains(subtask));
    }

    @Test
    void deleteAllKanbanTasks_shouldDeleteAllCreatedTasks() {
        Task task = mock(Task.class);
        taskRepository.createTask(task);
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        taskRepository.deleteAllKanbanTasks();
        assertTrue(taskRepository.getAllKanbanTasks().isEmpty());
    }
}
