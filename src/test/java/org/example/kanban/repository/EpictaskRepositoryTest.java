package org.example.kanban.repository;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EpictaskRepositoryTest {
    private KanbanRepository epictaskRepository;
    private KanbanRepository subtaskRepository;


    @BeforeEach
    void setUp() {
        epictaskRepository = new EpictaskRepositoryImpl();
        subtaskRepository = new SubtaskRepositoryImpl();
    }


    //createTask
    @Test
    void createEpictask_shouldInitializeTaskFields() {
        Epictask epictask = new Epictask();

        epictaskRepository.createTask(epictask);
        assertEquals(1, epictask.getId());
        assertEquals(TaskStatus.NEW.getStatusName(), epictask.getStatus());
    }

    //getTaskById
    @Test
    void getEpictaskById_shouldReturnTask_whenExists() {
        Epictask epictask = mock(Epictask.class);

        epictaskRepository.createTask(epictask);
        assertEquals(epictask, epictaskRepository.getTaskById(1).get());
    }

    @Test
    void getEpictaskById_shouldReturnEmptyOptional_whenNotExists() {
        assertTrue(epictaskRepository.getTaskById(1).isEmpty());
    }

    //getAllTasks
    @Test
    void getAllTasks_shouldReturnTaskCollection_whenEpictaskAdded() {
        Epictask epictask = mock(Epictask.class);

        epictaskRepository.createTask(epictask);
        assertEquals(new ArrayList<>(List.of(epictask)), new ArrayList<>(epictaskRepository.getAllTasks()));
    }

    //updateTask
    @Test
    void updateEpictask_shouldUpdateEpictaskFields() {
        Epictask oldTask = Epictask.epictaskBuilder().name("name").description("description").build();
        Epictask newTask = Epictask.epictaskBuilder().name("new name").description("new description").build();

        epictaskRepository.createTask(oldTask);
        epictaskRepository.updateTask(newTask, oldTask);

        assertEquals(newTask.getName(), oldTask.getName());
        assertEquals(newTask.getDescription(), oldTask.getDescription());
    }

    //deleteTaskById
    @Test
    void deleteEpictaskById_shouldDeleteEpictaskAndSubtasks() {
        Epictask epictask = mock(Epictask.class);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);

        epictaskRepository.deleteTaskById(1);
        assertTrue(epictaskRepository.getTaskById(1).isEmpty());
        assertTrue(subtaskRepository.getTaskById(2).isEmpty());
    }
}
