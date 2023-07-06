package org.example.kanban.repository;

import org.example.kanban.enum_.TaskStatus;
import org.example.kanban.model.Task;
import org.example.kanban.repository.inmemory.TaskInMemoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TaskRepositoryTest {
    private final KanbanRepository repository = new TaskInMemoryRepository();


    @AfterEach
    void tearDown() {
        repository.clearRepository();
    }


    //createTask
    @Test
    void createTask_shouldInitializeTaskFields() {
        Task task = new Task();

        repository.createTask(task);
        assertNotEquals(0, task.getId());
        assertEquals(TaskStatus.NEW.getStatusName(), task.getStatus());
    }

    //getTaskById
    @Test
    void getTaskById_shouldReturnTask_whenExists() {
        Task task = new Task();

        task = repository.createTask(task);
        assertEquals(task, repository.getTaskById(task.getId()).get());
    }

    @Test
    void getTaskById_shouldReturnEmptyOptional_whenNotExists() {
        assertTrue(repository.getTaskById(0).isEmpty());
    }

    //getAllTasks
    @Test
    void getAllTasks_shouldReturnTaskCollection_whenTaskAdded() {
        Task task = mock(Task.class);

        repository.createTask(task);
        assertTrue(repository.getAllTasks().contains(task));
    }

    //updateTask
    @Test
    void updateTask_shouldUpdateTaskFields() {
        Task oldTask = Task.builder().name("name").description("description").build();
        Task newTask = Task.builder().name("new name").description("new description").status("Done").build();

        oldTask = repository.createTask(oldTask);
        repository.updateTask(newTask, oldTask);

        Task actualTask = repository.getTaskById(oldTask.getId()).get();
        assertEquals(newTask.getName(), actualTask.getName());
        assertEquals(newTask.getDescription(), actualTask.getDescription());
        assertEquals(newTask.getStatus(), actualTask.getStatus());
    }

    //deleteTaskById
    @Test
    void deleteTaskById_shouldDeleteTask() {
        Task task = new Task();

        task = repository.createTask(task);

        repository.deleteTaskById(task.getId());
        assertTrue(repository.getTaskById(task.getId()).isEmpty());
    }
}
