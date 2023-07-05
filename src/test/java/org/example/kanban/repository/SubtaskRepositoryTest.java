package org.example.kanban.repository;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Epictask;
import org.example.kanban.model.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskRepositoryTest {
    private KanbanRepository epictaskRepository = new EpictaskRepositoryImpl();
    private KanbanRepository subtaskRepository = new SubtaskRepositoryImpl();


    @AfterEach
    void tearDown() {
        epictaskRepository.deleteAllKanbanTasks();
    }

    //createTask
    @Test
    void createSubtask_shouldInitializeSubtaskFields() {
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        assertNotEquals(0, subtask.getId());
        assertEquals(TaskStatus.NEW.getStatusName(), subtask.getStatus());
    }

    @Test
    void epictask_shouldContainsSubtask_whenSubtaskCreated() {
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        assertTrue(epictask.getSubtasks().contains(subtask));
    }

    //getTaskById
    @Test
    void getSubtaskById_shouldReturnSubtask_whenExists() {
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        assertEquals(subtask, subtaskRepository.getTaskById(subtask.getId()).get());
    }

    @Test
    void getSubtaskById_shouldReturnEmptyOptional_whenNotExists() {
        assertTrue(subtaskRepository.getTaskById(0).isEmpty());
    }

    //getAllTasks
    @Test
    void getAllTasks_shouldReturnSubtaskCollection_whenSubtaskAdded() {
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        assertTrue(subtaskRepository.getAllTasks().contains(subtask));
    }

    //updateTask
    @Test
    void updateSubtask_shouldUpdateSubtaskFields() {
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask oldTask = Subtask.subtaskBuilder().name("name").description("description").epictaskId(epictask.getId()).build();
        Subtask newTask = Subtask.subtaskBuilder().name("new name").description("new description").status("Done").build();
        subtaskRepository.createTask(oldTask);
        subtaskRepository.updateTask(newTask, oldTask);

        assertEquals(newTask.getName(), oldTask.getName());
        assertEquals(newTask.getDescription(), oldTask.getDescription());
        assertEquals(newTask.getStatus(), oldTask.getStatus());
    }

    @Test
    void epictaskStatus_shouldBeNew_whenAllSubtaskStatusIsNew() {
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask1 = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        Subtask subtask2 = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask1);
        subtaskRepository.createTask(subtask2);

        assertEquals("New", epictask.getStatus());
    }

    @Test
    void epictaskStatus_shouldBeDone_whenAllSubtaskStatusIsDone() {
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask1 = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        Subtask subtask2 = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
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
        epictaskRepository.createTask(epictask);
        Subtask subtask1 = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        Subtask subtask2 = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        Subtask subtask3 = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
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
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        subtaskRepository.deleteTaskById(subtask.getId());
        assertTrue(subtaskRepository.getTaskById(subtask.getId()).isEmpty());
    }

    @Test
    void deleteSubtaskById_shouldDeleteSubtaskFromEpictask() {
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);

        subtaskRepository.deleteTaskById(subtask.getId());
        assertFalse(epictask.getSubtasks().contains(subtask));
    }

    @Test
    void deleteSubtaskById_shouldUpdateEpictaskStatus() {
        Epictask epictask = new Epictask();
        epictaskRepository.createTask(epictask);
        Subtask subtask = Subtask.subtaskBuilder().epictaskId(epictask.getId()).build();
        subtaskRepository.createTask(subtask);
        subtaskRepository.updateTask(Subtask.subtaskBuilder().status("Done").build(), subtask);

        subtaskRepository.deleteTaskById(subtask.getId());
        assertEquals("New", epictask.getStatus());
    }
}
