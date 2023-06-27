package org.example.kanban.service;

import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Task;
import org.example.kanban.repository.KanbanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KanbanServiceImplTest {
    @Mock
    private KanbanRepository mockRepository;
    private KanbanService service;


    @BeforeEach
    void setUp() {
        service = new KanbanServiceImpl(mockRepository);
    }


    //createTask
    @Test
    void createTask_shouldCreateTask() {
        Task task = Task.builder().name("name").description("description").build();

        service.createTask(task);
        verify(mockRepository).createTask(task);
    }

    //getTaskById
    @Test
    void getTaskById_shouldReturnTask() {
        Task task = mock(Task.class);
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(task));

        assertEquals(task, service.getTaskById(1));
    }

    //getAllTasks
    @Test
    void getAllTasks_shouldReturnCollection() {
        service.getAllTasks();
        verify(mockRepository).getAllTasks();
    }

    //updateTask
    @Test
    void updateTask_shouldUpdateTask() {
        Task task = mock(Task.class);
        Task newTask = Task.builder().name("name").description("description").build();
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(task));

        service.updateTask(newTask, 1);
        verify(mockRepository).updateTask(newTask, task);
    }

    //deleteTaskById
    @Test
    void deleteTaskByID_shouldDeleteTaskById() {
        Task task = mock(Task.class);
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(task));

        service.deleteTaskById(1);
        verify(mockRepository).deleteTaskById(1);
    }

    //deleteAllTasks
    @Test
    void deleteAllTasks_shouldDeleteAllTasks() {
        service.deleteAllTasks();
        verify(mockRepository).deleteAllTasks();
    }

    //validateTask
    @Test
    void validateTask_shouldThrowValidationException_whenAddingTaskWithWrongName() {
        Task task = Task.builder().name(null).build();
        assertThrows(ValidationException.class, () -> service.createTask(task));

        task.setName("");
        assertThrows(ValidationException.class, () -> service.createTask(task));

        task.setName(String.copyValueOf(new char[26]));
        assertThrows(ValidationException.class, () -> service.createTask(task));
    }

    @Test
    void validateTask_shouldThrowValidationException_whenAddingTaskWithWrongDescription() {
        Task task = Task.builder().name("name").description(null).build();
        assertThrows(ValidationException.class, () -> service.createTask(task));

        task.setDescription("");
        assertThrows(ValidationException.class, () -> service.createTask(task));

        task.setDescription(String.copyValueOf(new char[251]));
        assertThrows(ValidationException.class, () -> service.createTask(task));
    }

    //getTaskById Optional
    @Test
    void getTaskById_shouldThrowValidationException_whenNoTasks() {
        assertThrows(ValidationException.class, () -> service.getTaskById(1));
    }
}
