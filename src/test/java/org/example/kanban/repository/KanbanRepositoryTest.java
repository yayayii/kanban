package org.example.kanban.repository;

import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class KanbanRepositoryTest {
    private KanbanRepository taskRepository;
    private KanbanRepository subtaskRepository;
    private KanbanRepository epictaskRepository;


    @BeforeEach
    void setUp() {
        taskRepository = new TaskRepositoryImpl();
        subtaskRepository = new SubtaskRepositoryImpl();
        epictaskRepository = new EpictaskRepositoryImpl();
    }


    @Test
    void getAllKanbanTasks_shouldReturnAllCreatedTasks() {
        Task task = mock(Task.class);
        Epictask epictask = mock(Epictask.class);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(2).build();

        taskRepository.createTask(task);
        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);

        assertEquals(
                new ArrayList<>(List.of(task, epictask, subtask)),
                new ArrayList<>(taskRepository.getAllKanbanTasks())
        );
        assertEquals(
                new ArrayList<>(List.of(task, epictask, subtask)),
                new ArrayList<>(subtaskRepository.getAllKanbanTasks())
        );
        assertEquals(
                new ArrayList<>(List.of(task, epictask, subtask)),
                new ArrayList<>(epictaskRepository.getAllKanbanTasks())
        );
    }

    @Test
    void deleteAllKanbanTasks_shouldDeleteAllCreatedTasks() {
        Task task = mock(Task.class);
        Epictask epictask = mock(Epictask.class);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(2).build();

        taskRepository.createTask(task);
        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);

        taskRepository.deleteAllKanbanTasks();
        assertEquals(new ArrayList<>(), new ArrayList<>(taskRepository.getAllKanbanTasks()));
    }
}
