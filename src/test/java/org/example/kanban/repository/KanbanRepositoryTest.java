package org.example.kanban.repository;

import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;
import org.example.kanban.repository.implementation.EpictaskInMemoryRepository;
import org.example.kanban.repository.implementation.SubtaskInMemoryRepository;
import org.example.kanban.repository.implementation.TaskInMemoryRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class KanbanRepositoryTest {
    private final KanbanRepository taskRepository = new TaskInMemoryRepository();
    private final KanbanRepository subtaskRepository = new SubtaskInMemoryRepository();
    private final KanbanRepository epictaskRepository = new EpictaskInMemoryRepository();


    @Test
    void getAllKanbanTasks_shouldReturnAllCreatedTasksAndEpicTasks() {
        Task task = mock(Task.class);
        taskRepository.createTask(task);
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        assertTrue(taskRepository.getRepository().contains(task));
        assertTrue(taskRepository.getRepository().contains(epictask));
        assertFalse(taskRepository.getRepository().contains(subtask));
    }

    @Test
    void deleteAllKanbanTasks_shouldDeleteAllCreatedTasks() {
        Task task = mock(Task.class);
        taskRepository.createTask(task);
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        taskRepository.clearRepository();
        assertTrue(taskRepository.getRepository().isEmpty());
    }
}
