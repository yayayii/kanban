package org.example.kanban.repository;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TaskRepositoryTest {
    private KanbanRepository repository;


    @BeforeEach
    void setUp() {
        repository = new TaskRepositoryImpl();
    }


    //createTask
    @Test
    void createTask_shouldInitializeTaskFields() {
        Task task = new Task();

        repository.createTask(task);
        assertEquals(1, task.getId());
        assertEquals(TaskStatus.NEW.getStatusName(), task.getStatus());
    }

    //getTaskById
    @Test
    void getTaskById_shouldReturnTask_whenExists() {
        Task task = mock(Task.class);

        repository.createTask(task);
        assertEquals(task, repository.getTaskById(1).get());
    }

    @Test
    void getTaskById_shouldReturnEmptyOptional_whenNotExists() {
        assertTrue(repository.getTaskById(1).isEmpty());
    }

    //getAllTasks
    @Test
    void getAllTasks_shouldReturnTaskCollection_whenTaskAdded() {
        Task task = mock(Task.class);

        repository.createTask(task);
        assertEquals(new ArrayList<>(List.of(task)), new ArrayList<>(repository.getAllTasks()));
    }

    //updateTask
    @Test
    void updateTask_shouldUpdateTaskFields() {
        Task oldTask = Task.builder().name("name").description("description").build();
        Task newTask = Task.builder().name("new name").description("new description").status("Done").build();

        repository.createTask(oldTask);
        repository.updateTask(newTask, oldTask);

        Task actualTask = repository.getTaskById(1).get();
        assertEquals(newTask.getName(), actualTask.getName());
        assertEquals(newTask.getDescription(), actualTask.getDescription());
        assertEquals(newTask.getStatus(), actualTask.getStatus());
    }

    //deleteTaskById
    @Test
    void deleteTaskById_shouldDeleteTask() {
        Task task = mock(Task.class);

        repository.createTask(task);

        repository.deleteTaskById(1);
        assertTrue(repository.getTaskById(1).isEmpty());
    }
}
