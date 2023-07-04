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

public class SubtaskRepositoryTest {
    private KanbanRepository epictaskRepository;
    private KanbanRepository subtaskRepository;


    @BeforeEach
    void setUp() {
        epictaskRepository = new EpictaskRepositoryImpl();
        subtaskRepository = new SubtaskRepositoryImpl();
    }


    //createTask
    @Test
    void createSubtask_shouldInitializeSubtaskFields() {
        Epictask epictask = mock(Epictask.class);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);

        assertEquals(2, subtask.getId());
        assertEquals(TaskStatus.NEW.getStatusName(), subtask.getStatus());
    }

    @Test
    void epictask_shouldContainsSubtask_whenSubtaskCreated() {
        Epictask epictask = new Epictask();
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);

        assertTrue(epictask.getSubtasks().contains(subtask));
    }

    //getTaskById
    @Test
    void getSubtaskById_shouldReturnSubtask_whenExists() {
        Epictask epictask = mock(Epictask.class);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);

        assertEquals(subtask, subtaskRepository.getTaskById(2).get());
    }

    @Test
    void getSubtaskById_shouldReturnEmptyOptional_whenNotExists() {
        assertTrue(subtaskRepository.getTaskById(1).isEmpty());
    }

    //getAllTasks
    @Test
    void getAllTasks_shouldReturnSubtaskCollection_whenSubtaskAdded() {
        Epictask epictask = mock(Epictask.class);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);

        assertEquals(new ArrayList<>(List.of(subtask)), new ArrayList<>(subtaskRepository.getAllTasks()));
    }

    //updateTask
    @Test
    void updateSubtask_shouldUpdateSubtaskFields() {
        Epictask epictask = mock(Epictask.class);
        Subtask oldTask = Subtask.subtaskBuilder().name("name").description("description").epictaskId(1).build();
        Subtask newTask = Subtask.subtaskBuilder().name("new name").description("new description").status("Done").build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(oldTask);
        subtaskRepository.updateTask(newTask, oldTask);

        assertEquals(newTask.getName(), oldTask.getName());
        assertEquals(newTask.getDescription(), oldTask.getDescription());
        assertEquals(newTask.getStatus(), oldTask.getStatus());
    }

    @Test
    void epictaskStatus_shouldBeDone_whenAllSubtaskStatusIsNew() {
        Epictask epictask = new Epictask();
        Subtask subtask1 = Subtask.subtaskBuilder().epictaskId(1).build();
        Subtask subtask2 = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask1);
        subtaskRepository.createTask(subtask2);

        assertEquals("New", epictask.getStatus());
    }

    @Test
    void epictaskStatus_shouldBeDone_whenAllSubtaskStatusIsDone() {
        Epictask epictask = new Epictask();
        Subtask subtask1 = Subtask.subtaskBuilder().epictaskId(1).build();
        Subtask subtask2 = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask1);
        subtaskRepository.createTask(subtask2);
        subtaskRepository.updateTask(Subtask.subtaskBuilder().status("Done").build(), subtask1);
        subtaskRepository.updateTask(Subtask.subtaskBuilder().status("Done").build(), subtask2);

        assertEquals("Done", epictask.getStatus());
    }

    @Test
    void epictaskStatus_shouldBeInProgress_whenSubtaskStatusIsDifferent() {
        Epictask epictask = new Epictask();
        Subtask subtask1 = Subtask.subtaskBuilder().epictaskId(1).build();
        Subtask subtask2 = Subtask.subtaskBuilder().epictaskId(1).build();
        Subtask subtask3 = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask1);
        subtaskRepository.createTask(subtask2);
        subtaskRepository.createTask(subtask3);
        subtaskRepository.updateTask(Subtask.subtaskBuilder().status("New").build(), subtask1);
        subtaskRepository.updateTask(Subtask.subtaskBuilder().status("Done").build(), subtask2);
        subtaskRepository.updateTask(Subtask.subtaskBuilder().status("In progress").build(), subtask3);

        assertEquals("In progress", epictask.getStatus());
    }

    //deleteTaskById
    @Test
    void deleteSubtaskById_shouldDeleteSubtask() {
        Epictask epictask = new Epictask();
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);

        subtaskRepository.deleteTaskById(2);
        assertTrue(subtaskRepository.getTaskById(2).isEmpty());
    }

    @Test
    void deleteSubtaskById_shouldDeleteSubtaskFromEpictask() {
        Epictask epictask = new Epictask();
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);

        subtaskRepository.deleteTaskById(2);
        assertFalse(epictask.getSubtasks().contains(subtask));
    }

    @Test
    void deleteSubtaskById_shouldUpdateEpictaskStatus() {
        Epictask epictask = new Epictask();
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(1).build();

        epictaskRepository.createTask(epictask);
        subtaskRepository.createTask(subtask);
        subtaskRepository.updateTask(Subtask.subtaskBuilder().status("Done").build(), subtask);

        subtaskRepository.deleteTaskById(2);
        assertEquals("New", epictask.getStatus());
    }
}
