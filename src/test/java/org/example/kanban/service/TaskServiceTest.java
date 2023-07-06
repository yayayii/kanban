package org.example.kanban.service;

import org.example.kanban.exception.ValidationException;
import org.example.kanban.model.Subtask;
import org.example.kanban.model.Task;
import org.example.kanban.repository.KanbanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private KanbanRepository mockRepository;
    private KanbanService service;


    @BeforeEach
    void setUp() {
        service = new TaskService(mockRepository);
    }


    //validation exception
    @Test
    void createTask_shouldThrowValidationException_whenTaskNameIsNull() {
        Task task = new Task();
        assertThrows(ValidationException.class, () -> service.createTask(task));
    }

    @Test
    void createTask_shouldThrowValidationException_whenTaskNameIsEmpty() {
        Task task = Task.builder().name("").build();
        assertThrows(ValidationException.class, () -> service.createTask(task));
    }

    @Test
    void createTask_shouldThrowValidationException_whenTaskNameIsTooLong() {
        Task task = Task.builder().name(String.copyValueOf(new char[26])).build();
        assertThrows(ValidationException.class, () -> service.createTask(task));
    }

    @Test
    void createTask_shouldThrowValidationException_whenTaskDescriptionIsNull() {
        Task task = Task.builder().name("name").build();
        assertThrows(ValidationException.class, () -> service.createTask(task));
    }

    @Test
    void createTask_shouldThrowValidationException_whenTaskDescriptionIsEmpty() {
        Task task = Task.builder().name("name").description("").build();
        assertThrows(ValidationException.class, () -> service.createTask(task));
    }

    @Test
    void createTask_shouldThrowValidationException_whenTaskDescriptionIsTooLong() {
        Task task = Task.builder().name("name").description(String.copyValueOf(new char[251])).build();
        assertThrows(ValidationException.class, () -> service.createTask(task));
    }

    @Test
    void createTask_shouldThrowValidationException_whenTaskStatusIsWrong() {
        Task task = Task.builder().name("name").description("description").status("Qwe").build();
        assertThrows(ValidationException.class, () -> service.createTask(task));
    }

    //createTask
    @Test
    void createTask_shouldReturnTask() {
        Task task = Task.builder().name("name").description("description").build();
        when(mockRepository.createTask(any())).thenReturn(task);

        assertEquals(task, service.createTask(task));
    }

    //getTaskById
    @Test
    void getTaskById_shouldThrowValidationException_whenOptionalIsEmpty() {
        assertThrows(ValidationException.class, () -> service.getTaskById(1));
    }

    @Test
    void getTaskById_shouldReturnTask() {
        Task task = mock(Task.class);
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(task));

        assertEquals(task, service.getTaskById(1));
    }

    //getAllTasks
    @Test
    void getAllTasks_shouldReturnSet() {
        Set<Task> set = Collections.emptySet();
        when(mockRepository.getAllTasks()).thenReturn(set);

        assertEquals(set, service.getAllTasks());
    }

    //getAllKanbanTasks
    @Test
    void getAllKanbanTasks_shouldReturnSet() {
        Set<Task> set = Collections.emptySet();
        when(mockRepository.getRepository()).thenReturn(set);

        assertEquals(set, service.getAllKanbanTasks());
    }

    //updateTask
    @Test
    void updateTask_shouldThrowValidationException_whenOptionalIsEmpty() {
        Task task = mock(Task.class);
        assertThrows(ValidationException.class, () -> service.updateTask(task, 1));
    }

    @Test
    void updateTask_shouldReturnTask() {
        Task task = Task.builder().name("name").description("description").build();
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(task));
        doNothing().when(mockRepository).updateTask(any(), any());

        assertEquals(task, service.updateTask(task, 1));
    }

    //deleteTaskById
    @Test
    void deleteTaskById_shouldThrowValidationException_whenOptionalIsEmpty() {
        assertThrows(ValidationException.class, () -> service.deleteTaskById(1));
    }

    @Test
    void deleteTaskById_shouldCallMethod() {
        Task task = mock(Task.class);
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(task));
        doNothing().when(mockRepository).deleteTaskById(anyLong());

        service.deleteTaskById(1);
        verify(mockRepository).deleteTaskById(1);
    }

    //deleteAllKanbanTasks
    @Test
    void deleteAllKanbanTasks_shouldCallMethod() {
        doNothing().when(mockRepository).clearRepository();

        service.deleteAllKanbanTasks();
        verify(mockRepository).clearRepository();
    }

    //createSubtask
    @Test
    void createSubtask_shouldThrowValidationException_whenOptionalIsEmpty() {
        KanbanService service = new SubtaskService(mockRepository);
        Subtask subtask = Subtask.subtaskBuilder().name("name").description("description").build();

        assertThrows(ValidationException.class, () -> service.createTask(subtask));
    }

    @Test
    void createSubtask_shouldReturnTask() {
        KanbanService service = new SubtaskService(mockRepository);
        Subtask subtask = Subtask.subtaskBuilder().name("name").description("description").build();
        when(mockRepository.getTaskById(anyLong())).thenReturn(Optional.of(subtask));

        assertEquals(subtask, service.createTask(subtask));
    }
}
