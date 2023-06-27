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
    private KanbanService kanbanService;


    @BeforeEach
    void beforeEach() {
        kanbanService = new KanbanServiceImpl(mockRepository);
    }


    @Test
    void createTask_shouldCreateTask() {
        Task task = Task.builder().name("name").description("description").build();

        kanbanService.createTask(task);
        verify(mockRepository).createTask(task);
    }

    @Test
    void getTaskById_shouldReturnTask() {
        Task task = mock(Task.class);
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(task));

        assertEquals(task, kanbanService.getTaskById(1));
    }

    @Test
    void getAllTasks_shouldReturnCollection() {
        kanbanService.getAllTasks();
        verify(mockRepository).getAllTasks();
    }

    @Test
    void updateTask_shouldUpdateTask() {
        Task task = mock(Task.class);
        Task newTask = Task.builder().name("name").description("description").build();
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(task));

        kanbanService.updateTask(newTask, 1);
        verify(mockRepository).updateTask(newTask, task);
    }

    @Test
    void deleteTaskByID_shouldDeleteTaskById() {
        Task task = mock(Task.class);
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(task));

        kanbanService.deleteTaskById(1);
        verify(mockRepository).deleteTaskById(1);
    }

    @Test
    void deleteAllTasks_shouldDeleteAllTasks() {
        kanbanService.deleteAllTasks();
        verify(mockRepository).deleteAllTasks();
    }

    //test validateTask
    @Test
    void validateTask_shouldThrowValidationException_whenAddingTaskWithWrongName() {
        Task task = Task.builder().name(null).build();
        assertThrows(ValidationException.class, () -> kanbanService.createTask(task));

        task.setName("");
        assertThrows(ValidationException.class, () -> kanbanService.createTask(task));

        task.setName(String.copyValueOf(new char[26]));
        assertThrows(ValidationException.class, () -> kanbanService.createTask(task));
    }

    @Test
    void validateTask_shouldThrowValidationException_whenAddingTaskWithWrongDescription() {
        Task task = Task.builder().name("name").description(null).build();
        assertThrows(ValidationException.class, () -> kanbanService.createTask(task));

        task.setDescription("");
        assertThrows(ValidationException.class, () -> kanbanService.createTask(task));

        task.setDescription(String.copyValueOf(new char[251]));
        assertThrows(ValidationException.class, () -> kanbanService.createTask(task));
    }

    //test optional getTaskById
    @Test
    void getTaskById_shouldThrowValidationException_whenNoTasks() {
        assertThrows(ValidationException.class, () -> kanbanService.getTaskById(1));
    }
}
