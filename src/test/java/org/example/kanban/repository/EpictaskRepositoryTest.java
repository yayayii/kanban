package org.example.kanban.repository;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.example.kanban.repository.implementation.EpictaskInMemoryRepository;
import org.example.kanban.repository.implementation.SubtaskInMemoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EpictaskRepositoryTest {
    private final KanbanRepository epictaskRepository = new EpictaskInMemoryRepository();
    private final KanbanRepository subtaskRepository = new SubtaskInMemoryRepository();


    @AfterEach
    void tearDown() {
        epictaskRepository.clearRepository();
    }


    //createTask
    @Test
    void createEpictask_shouldInitializeTaskFields() {
        Epictask epictask = new Epictask();

        epictaskRepository.createTask(epictask);
        assertNotEquals(0, epictask.getId());
        assertEquals(TaskStatus.NEW.getStatusName(), epictask.getStatus());
    }

    //getTaskById
    @Test
    void getEpictaskById_shouldReturnTask_whenExists() {
        Epictask epictask = new Epictask();

        epictaskRepository.createTask(epictask);
        assertEquals(epictask, epictaskRepository.getTaskById(epictask.getId()).get());
    }

    @Test
    void getEpictaskById_shouldReturnEmptyOptional_whenNotExists() {
        assertTrue(epictaskRepository.getTaskById(0).isEmpty());
    }

    //getAllTasks
    @Test
    void getAllTasks_shouldReturnTaskCollection_whenEpictaskAdded() {
        Epictask epictask = mock(Epictask.class);

        epictaskRepository.createTask(epictask);
        assertTrue(epictaskRepository.getAllTasks().contains(epictask));
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
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        epictaskRepository.deleteTaskById(epictask.getId());
        assertTrue(epictaskRepository.getTaskById(epictask.getId()).isEmpty());
        assertTrue(subtaskRepository.getTaskById(subtask.getId()).isEmpty());
    }
}
